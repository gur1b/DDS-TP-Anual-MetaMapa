document.addEventListener('DOMContentLoaded', () => {
    const main = document.querySelector('.admin-main-content');
    const list = document.getElementById('hechos-list');
    if (!list) return;

    // CSRF (Spring Security)
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.content || null;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content || 'X-CSRF-TOKEN';

    // Buscar
    const searchInput = document.getElementById('search-input');
    const doFilter = () => {
        const q = (searchInput.value || '').trim().toLowerCase();
        list.querySelectorAll('.hecho-card').forEach(card => {
            const nombre = (card.dataset.nombre || '').toLowerCase();
            const desc = (card.dataset.descripcion || '').toLowerCase();
            card.style.display = (nombre.includes(q) || desc.includes(q)) ? '' : 'none';
        });
    };
    searchInput?.addEventListener('input', doFilter);
    document.getElementById('search-btn')?.addEventListener('click', (e) => { e.preventDefault(); doFilter(); });

    // Modal delete (con fallback)
    const deleteModal = document.getElementById('delete-modal');
    const btnCancelDelete = document.getElementById('cancel-delete');
    const btnConfirmDelete = document.getElementById('confirm-delete');
    let pendingDeleteForm = null;

    const openDeleteModal = (form) => {
        if (!form) {
            console.warn('No se encontró el form.delete-form en esta card');
            return;
        }
        pendingDeleteForm = form;
        if (!deleteModal) {
            // Fallback sin modal
            if (window.confirm('¿Confirmar eliminación?')) {
                if (pendingDeleteForm.requestSubmit) pendingDeleteForm.requestSubmit();
                else pendingDeleteForm.submit();
            }
            return;
        }
        // Mostrar modal
        deleteModal.classList.add('active');
    };

    const closeDeleteModal = () => {
        pendingDeleteForm = null;
        deleteModal?.classList.remove('active');
    };

    btnCancelDelete?.addEventListener('click', closeDeleteModal);
    btnConfirmDelete?.addEventListener('click', () => {
        if (!pendingDeleteForm) {
            console.warn('No hay form pendiente de eliminación');
            return;
        }
        // Cerrar modal para feedback visual y enviar
        deleteModal?.classList.remove('active');
        if (pendingDeleteForm.requestSubmit) pendingDeleteForm.requestSubmit();
        else pendingDeleteForm.submit();
    });

    // Cerrar modal con ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && deleteModal?.classList.contains('active')) {
            closeDeleteModal();
        }
    });

    // Delegación de clicks
    list.addEventListener('click', async (e) => {
        const target = e.target.closest('button, .icon-btn');
        if (!target) return;

        const card = e.target.closest('.hecho-card');
        if (!card) return;

        // Eliminar (abre modal)
        if (target.classList.contains('delete-btn')) {
            const form = card.querySelector('form.delete-form');
            openDeleteModal(form);
            return;
        }

        // Editar
        if (target.classList.contains('edit-btn') && !card.classList.contains('is-editing')) {
            // Pre-cargar etiquetas CSV leyendo los chips actuales
            const chips = card.querySelectorAll('.tags.view-mode .chip');
            const csv = Array.from(chips).map(c => c.textContent.trim()).join(', ');
            const etInput = card.querySelector('input.edit-mode[name="etiquetas"]');
            if (etInput) etInput.value = csv;

            card.classList.add('is-editing');
            main.classList.add('child-is-editing');
            snapshotInputs(card);
            return;
        }

        // Cancelar
        if (target.classList.contains('cancel-btn') && card.classList.contains('is-editing')) {
            restoreSnapshot(card);
            card.classList.remove('is-editing');
            main.classList.remove('child-is-editing');
            return;
        }

        // Guardar
        if (target.classList.contains('save-btn') && card.classList.contains('is-editing')) {
            target.disabled = true;
            try {
                await saveCard(card);
                card.classList.remove('is-editing');
                main.classList.remove('child-is-editing');
            } catch (err) {
                alert('Error guardando cambios. Revisá la consola/Network.');
                console.error(err);
            } finally {
                target.disabled = false;
            }
            return;
        }
    });

    // Snapshot helpers
    function snapshotInputs(card) {
        card.querySelectorAll('.edit-mode[name]').forEach(inp => {
            inp.dataset._original = inp.value;
        });
    }
    function restoreSnapshot(card) {
        card.querySelectorAll('.edit-mode[name]').forEach(inp => {
            if (inp.dataset._original != null) inp.value = inp.dataset._original;
        });
    }

    // Extraer identificador/hash del card con múltiples estrategias
    function getIdentFromCard(card) {
        // 1) data-hash
        const dh = card.dataset.hash?.trim();
        if (dh) return dh;

        // 2) hidden input
        const hidden = card.querySelector('input.hash-holder')?.value?.trim();
        if (hidden) return hidden;

        // 3) del action del form de eliminar
        const form = card.querySelector('form.delete-form');
        if (form?.action) {
            try {
                const u = new URL(form.action, window.location.origin);
                const m = u.pathname.match(/\/admin\/hechos\/(.+?)\/eliminar$/);
                if (m && m[1]) return decodeURIComponent(m[1]);
            } catch (_) {}
        }

        // 4) id="hecho-<ident>"
        if (card.id?.startsWith('hecho-')) {
            const fromId = card.id.slice('hecho-'.length).trim();
            if (fromId && fromId !== 'no-hash') return fromId;
        }

        return null;
    }

    // Guardar PATCH
    async function saveCard(card) {
        const ident = getIdentFromCard(card);
        if (!ident) {
            alert('Este hecho no tiene identificador (hash). No se puede guardar.');
            throw new Error('Sin identificador/hash en el card');
        }

        const nombre = card.querySelector('input.edit-mode[name="nombre"]')?.value?.trim();
        const descripcion = card.querySelector('.edit-mode[name="descripcion"]')?.value?.trim();
        const etiquetasCsv = card.querySelector('input.edit-mode[name="etiquetas"]')?.value?.trim();

        const payload = {
            nombre: nombre || null,
            descripcion: descripcion || null,
            etiquetas: (etiquetasCsv || '')
                .split(',')
                .map(s => s.trim())
                .filter(Boolean)
        };

        const headers = { 'Content-Type': 'application/json' };
        if (csrfToken) headers[csrfHeader] = csrfToken;

        const resp = await fetch(`/admin/hechos/${encodeURIComponent(ident)}/modificar`, {
            method: 'PATCH',
            headers,
            body: JSON.stringify(payload)
        });

        if (!resp.ok) {
            const txt = await resp.text().catch(() => '');
            throw new Error(`PATCH failed: ${resp.status} ${txt}`);
        }

        // Refrescar UI
        if (nombre != null) {
            const titleEl = card.querySelector('[data-field="nombre"]');
            if (titleEl) titleEl.textContent = nombre;
            card.dataset.nombre = nombre;
        }
        if (descripcion != null) {
            const descEl = card.querySelector('[data-field="descripcion"]');
            if (descEl) descEl.textContent = descripcion;
            card.dataset.descripcion = descripcion;
        }
        const tagsWrap = card.querySelector('.tags.view-mode');
        if (tagsWrap) {
            tagsWrap.innerHTML = '';
            (etiquetasCsv || '')
                .split(',')
                .map(s => s.trim())
                .filter(Boolean)
                .forEach(tag => {
                    const span = document.createElement('span');
                    span.className = 'chip';
                    span.textContent = tag;
                    tagsWrap.appendChild(span);
                });
        }
    }
});