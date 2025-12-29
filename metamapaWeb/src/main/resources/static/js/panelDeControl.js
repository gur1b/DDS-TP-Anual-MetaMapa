document.addEventListener('DOMContentLoaded', () => {
  // Limitar fecha máxima al día actual en los campos de fecha del modal de crear colección
  const fechaInputs = [
    document.getElementById('new-criterio-suceso-desde'),
    document.getElementById('new-criterio-suceso-hasta'),
    document.getElementById('new-criterio-carga-desde'),
    document.getElementById('new-criterio-carga-hasta')
  ];
  const hoy = new Date();
  const yyyy = hoy.getFullYear();
  const mm = String(hoy.getMonth() + 1).padStart(2, '0');
  const dd = String(hoy.getDate()).padStart(2, '0');
  const maxDate = `${yyyy}-${mm}-${dd}`;
  fechaInputs.forEach(input => {
    if (input) input.setAttribute('max', maxDate);
  });
  const adminMainContent = document.querySelector('.admin-main-content');
  const list = document.querySelector('.collections-list');

  // =========================
  //   HELPER: CSRF Token
  // =========================
  const getCsrfToken = () => {
    const tokenInput = document.querySelector('input[name="_csrf"]');
    return tokenInput ? tokenInput.value : null;
  };

  // =========================
  //   MODAL ELIMINAR
  // =========================
  const modalDelete = document.getElementById('delete-modal');
  const cancelBtnDelete = document.getElementById('cancel-delete');
  const confirmBtnDelete = document.getElementById('confirm-delete');
  let currentCardForDelete = null;

  function openDeleteModal(card) {
    currentCardForDelete = card;
    modalDelete?.classList.add('active');
  }
  function closeDeleteModal() {
    modalDelete?.classList.remove('active');
    currentCardForDelete = null;
  }

  confirmBtnDelete?.addEventListener('click', (e) => {
    e.preventDefault();
    if (!currentCardForDelete) return;

    const form = currentCardForDelete.querySelector('form.delete-form');
    if (!form) { closeDeleteModal(); return; }

    const url = form.getAttribute('action');
    const token = getCsrfToken();

    fetch(url, {
      method: 'POST',
      headers: { 'X-CSRF-TOKEN': token },
      redirect: 'manual'
    })
        .then(resp => {
          if (resp.ok || resp.type === 'opaqueredirect') {
            currentCardForDelete.remove();
            console.log('Colección eliminada exitosamente.');
          } else {
            alert('No se pudo eliminar la colección.');
          }
        })
        .catch(err => {
          console.error(err);
          alert('Error de conexión.');
        })
        .finally(() => closeDeleteModal());
  });

  cancelBtnDelete?.addEventListener('click', (e) => { e.preventDefault(); closeDeleteModal(); });
  modalDelete?.addEventListener('click', (e) => { if (e.target.id === 'delete-modal') closeDeleteModal(); });


  // ==========================================
  //   LÓGICA DEL MODAL DE AGREGAR CRITERIO
  // ==========================================
  const modalCriteria = document.getElementById('modal-add-criteria');
  const selectType = document.getElementById('select-criteria-type');
  const containerInputs = document.getElementById('criteria-inputs-container');
  const btnCancelCrit = document.getElementById('btn-cancel-criteria');
  const btnConfirmCrit = document.getElementById('btn-confirm-criteria');

  let targetUlForCriteria = null;

  // Abrir Modal (Edición y Creación)
  document.addEventListener('click', (e) => {
    // Botón "Agregar Criterio"
    if (e.target.closest('.btn-open-criteria-modal') || e.target.closest('#btn-add-crit-creation')) {
      e.preventDefault();
      // Si es desde una tarjeta (edición)
      if (e.target.closest('.collection-card')) {
        const card = e.target.closest('.collection-card');
        targetUlForCriteria = card.querySelector('.criteria-list-edit');
      }
      // Si es desde el modal de crear nueva colección
      else if (e.target.closest('#add-modal')) {
        targetUlForCriteria = document.getElementById('new-collection-criteria-list');
      }

      resetCriteriaModal();
      modalCriteria.classList.add('active');
    }

    // Botón "Eliminar Criterio" (Delegación para listas dinámicas)
    if (e.target.classList.contains('btn-delete-crit')) {
      e.preventDefault();
      const li = e.target.closest('li');
      if (li) li.remove();
    }
  });

  const closeCriteriaModal = () => modalCriteria.classList.remove('active');
  btnCancelCrit?.addEventListener('click', (e) => { e.preventDefault(); closeCriteriaModal(); });

  // Mostrar inputs según tipo
  selectType?.addEventListener('change', () => {
    const type = selectType.value;
    containerInputs.style.display = 'block';
    document.querySelectorAll('.dynamic-group').forEach(el => el.classList.add('hidden'));

    if (type === 'nombre' || type === 'descripcion') {
      document.getElementById('input-group-text').classList.remove('hidden');
    } else if (type === 'categoria') {
      document.getElementById('input-group-cat').classList.remove('hidden');
    } else if (type === 'ubicacion') {
      document.getElementById('input-group-geo').classList.remove('hidden');
    } else if (type === 'fechaSuceso' || type === 'fechaCarga') {
      document.getElementById('input-group-date').classList.remove('hidden');
    } else if (type === 'horaSuceso') {
      document.getElementById('input-group-time').classList.remove('hidden');
    }
  });

  // Confirmar Criterio
  btnConfirmCrit?.addEventListener('click', (e) => {
    e.preventDefault();
    if (!targetUlForCriteria) return;
    const type = selectType.value;
    if (!type) { alert('Selecciona un tipo'); return; }

    let palabra=null, cat=null, lat=null, lon=null, desde=null, hasta=null, horaDesde=null, horaHasta=null;
    let displayText = "";

    if (type === 'nombre' || type === 'descripcion') {
      palabra = document.getElementById('crit-input-keyword').value;
      displayText = `${type}: ${palabra}`;
    } else if (type === 'categoria') {
      const select = document.getElementById('crit-select-category');
      const otra   = document.getElementById('crit-input-category-otra');

      if (!select.value) {
        alert('Elegí una categoría');
        return;
      }

      if (select.value === 'Otro') {
        cat = otra.value.trim();
        if (!cat) {
          alert('Especificá la categoría');
          return;
        }
      } else {
        cat = select.value;
      }

      displayText = `Categoría: ${cat}`;
    } else if (type === 'ubicacion') {
      lat = document.getElementById('crit-input-lat').value;
      lon = document.getElementById('crit-input-lon').value;
      displayText = `Ubicación: ${lat}, ${lon}`;
    } else if (type.startsWith('fecha')) {
      desde = document.getElementById('crit-input-from').value;
      hasta = document.getElementById('crit-input-to').value;
      displayText = `${type}: ${desde} al ${hasta}`;
    } else if (type === 'horaSuceso') {
    horaDesde = document.getElementById('crit-input-time-from').value;
    horaHasta = document.getElementById('crit-input-time-to').value;

    if (!horaDesde || !horaHasta) {
      alert('Completá hora desde y hora hasta');
      return;
    }

    displayText = `Hora suceso: ${horaDesde} a ${horaHasta}`;
  }

    const li = document.createElement('li');
    li.style.cssText = "display:flex; justify-content:space-between; align-items:center; padding:8px 12px; background:#f9fafb; border:1px solid #e5e7eb; border-radius:8px; margin-bottom:6px;";

    li.dataset.type = type;
    if(palabra) li.dataset.palabra = palabra;
    if(cat) li.dataset.categoria = cat;
    if(lat) li.dataset.lat = lat;
    if(lon) li.dataset.lon = lon;
    if(desde) li.dataset.desde = desde;
    if(hasta) li.dataset.hasta = hasta;
    if(horaDesde) li.dataset.horaDesde = horaDesde;
    if(horaHasta) li.dataset.horaHasta = horaHasta;


    li.innerHTML = `<span>${displayText}</span><button type="button" class="delete-source-btn btn-delete-crit">×</button>`;
    targetUlForCriteria.appendChild(li);
    closeCriteriaModal();
  });

  function resetCriteriaModal() {
    document.getElementById('form-add-criteria').reset();
    selectType.value = "";
    containerInputs.style.display = 'none';
    document.querySelectorAll('.dynamic-group').forEach(el => el.classList.add('hidden'));

    if (catOtraGroup) catOtraGroup.classList.add('hidden');
  }


  // =========================
  //   MODAL CREAR COLECCIÓN
  // =========================
  const modalAdd = document.getElementById('add-modal');
  const openBtnAdd = document.querySelector('.add-btn');
  const cancelBtnAdd = document.getElementById('cancel-add');
  const confirmBtnAdd = document.getElementById('confirm-add');

  openBtnAdd?.addEventListener('click', (e) => { e.preventDefault(); modalAdd?.classList.add('active'); });
  cancelBtnAdd?.addEventListener('click', (e) => { e.preventDefault(); modalAdd?.classList.remove('active'); });

  modalAdd?.addEventListener('click', (e) => {
    if (e.target.id === 'add-modal') modalAdd?.classList.remove('active');
  });

  confirmBtnAdd?.addEventListener('click', (e) => {
    e.preventDefault();

    const nombre = document.getElementById('new-nombre').value.trim();
    const info = document.getElementById('new-info').value.trim();

    if (!nombre) { alert('El nombre es obligatorio.'); return; }

    // Fuentes
    const fuentesSeleccionadas = [];
    document.querySelectorAll('input[name="fuentesSeleccionadas"]:checked').forEach((cb) => {
      fuentesSeleccionadas.push(parseInt(cb.value, 10));
    });

    // Criterios
    const criterios = [];
    document.querySelectorAll('#new-collection-criteria-list li').forEach(li => {
      const d = li.dataset;
      const c = { type: d.type };
      if(d.palabra) c.palabraClave = d.palabra;
      if(d.categoria) c.categoria = d.categoria;
      if(d.lat) c.latitud = parseFloat(d.lat);
      if(d.lon) c.longitud = parseFloat(d.lon);
      if(d.desde) c.desde = d.desde;
      if(d.hasta) c.hasta = d.hasta;
      if(d.horaDesde) c.horaDesde = d.horaDesde;
      if(d.horaHasta) c.horaHasta = d.horaHasta;
      criterios.push(c);
    });

    // Algoritmo y Modo
    const algoritmoConsenso = document.getElementById('new-algoritmo').value;
    const modoDeNavegacion = document.getElementById('new-modoNavegacion').value;

    if (modoDeNavegacion === 'CURADA' && (!algoritmoConsenso || algoritmoConsenso === 'SIN')) {
      alert('Las colecciones CURADAS necesitan un algoritmo de consenso.');
      return;
    }

    const payload = {
      titulo: nombre,
      descripcionColeccion: info,
      fuentes: fuentesSeleccionadas,
      criterioDePertenencia: criterios,
      algoritmoConsenso: algoritmoConsenso,
      modoDeNavegacion: modoDeNavegacion
    };

    fetch('/admin/colecciones/crear', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'X-CSRF-TOKEN': getCsrfToken() },
      body: JSON.stringify(payload)
    })
        .then(resp => {
          if (resp.ok) location.reload();
          else alert('No se pudo crear la colección. Revisa la consola.');
        });
  });


  // =========================
  //   EDICIÓN IN-PLACE
  // =========================
  if (!list) return;
  const getCard = (el) => el.closest('.collection-card');

  // --- INICIAR EDICIÓN ---
  const startEdit = (card) => {
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const descInput = card.querySelector('.card-body input[aria-label="Editar descripción"]');

    // CORREGIDO: Usar el selector por NAME, sin la clase edit-mode pegada (por si está en el padre)
    const algoSelect = card.querySelector('select[name="algoritmoConsensoEdit"]');
    const modoSelect = card.querySelector('select[name="modoDeNavegacionEdit"]');

    // Guardar originales
    if (tituloInput) card.dataset.originalTitulo = tituloInput.value;
    if (descInput) card.dataset.originalDesc = descInput.value;
    if (algoSelect) card.dataset.originalAlgo = algoSelect.value;
    if (modoSelect) card.dataset.originalModo = modoSelect.value;

    card.classList.add('is-editing');
    adminMainContent?.classList.add('child-is-editing');
  };

  // --- CANCELAR EDICIÓN ---
  const cancelEdit = (card) => {
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const tituloView = card.querySelector('.card-header .view-mode');
    const descInput = card.querySelector('.card-body input[aria-label="Editar descripción"]');
    const descView = descInput ? descInput.parentElement.querySelector('.view-mode') : null;

    const algoSelect = card.querySelector('select[name="algoritmoConsensoEdit"]');
    const modoSelect = card.querySelector('select[name="modoDeNavegacionEdit"]');

    // Restaurar
    if (tituloInput) {
      tituloInput.value = card.dataset.originalTitulo || "";
      if (tituloView) tituloView.textContent = card.dataset.originalTitulo || "";
    }
    if (descInput) {
      descInput.value = card.dataset.originalDesc || "";
      if (descView) descView.textContent = card.dataset.originalDesc || "";
    }
    if (algoSelect && card.dataset.originalAlgo !== undefined) {
      algoSelect.value = card.dataset.originalAlgo;
    }
    if (modoSelect && card.dataset.originalModo !== undefined) {
      modoSelect.value = card.dataset.originalModo;
    }

    card.classList.remove('is-editing');
    adminMainContent?.classList.remove('child-is-editing');
  };

  // --- GUARDAR EDICIÓN ---
  const saveEdit = (card) => {
    const id = card.id?.replace('card-', '');
    if (!id) return;

    // Selectores (Usamos nombres consistentes con HTML)
    const tituloInput = card.querySelector('.card-header .card-input.edit-mode');
    const descInput = card.querySelector('.card-body input[aria-label="Editar descripción"]');
    const algoSelect = card.querySelector('select[name="algoritmoConsensoEdit"]');
    const modoSelect = card.querySelector('select[name="modoDeNavegacionEdit"]');

    // Valores
    const nuevoTitulo = tituloInput ? tituloInput.value.trim() : null;
    const nuevaDesc = descInput ? descInput.value.trim() : null;
    const nuevoAlgo = (algoSelect && algoSelect.value !== "") ? algoSelect.value : null;
    const nuevoModo = modoSelect ? modoSelect.value : null;

    // Validación
    if (nuevoModo === 'CURADA' && (!nuevoAlgo || nuevoAlgo === 'SIN')) {
      alert('Las colecciones CURADAS necesitan un algoritmo de consenso.');
      return;
    }

    // Fuentes
    const fuentesSeleccionadas = [];
    card.querySelectorAll('input[name="fuentesSeleccionadasEdit"]:checked').forEach(cb => {
      fuentesSeleccionadas.push(parseInt(cb.value, 10));
    });

    // Criterios
    const criteriosFinales = [];
    card.querySelectorAll('.criteria-list-edit li').forEach(li => {
      const d = li.dataset;
      const c = { type: d.type };
      if(d.palabra) c.palabraClave = d.palabra;
      if(d.categoria) c.categoria = d.categoria;
      if(d.lat) c.latitud = parseFloat(d.lat);
      if(d.lon) c.longitud = parseFloat(d.lon);
      if(d.desde) c.desde = d.desde;
      if(d.hasta) c.hasta = d.hasta;
      if(d.horaDesde) c.horaDesde = d.horaDesde;
      if(d.horaHasta) c.horaHasta = d.horaHasta;
      criteriosFinales.push(c);
    });

    fetch(`/admin/colecciones/${id}/modificar`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json', 'X-CSRF-TOKEN': getCsrfToken() },
      body: JSON.stringify({
        titulo: nuevoTitulo,
        descripcionColeccion: nuevaDesc,
        fuentes: fuentesSeleccionadas,
        algoritmoConsenso: nuevoAlgo,
        modoDeNavegacion: nuevoModo,
        criterioDePertenencia: criteriosFinales
      })
    })
        .then(resp => {
          if (!resp.ok) throw new Error('Error en server');

          // Éxito: Recargar para ver cambios reflejados (especialmente textos de selects)
          console.log('Guardado OK');
          location.reload();
        })
        .catch(err => {
          console.error(err);
          alert('No se pudo guardar.');
          cancelEdit(card);
        });
  };

  // DELEGACIÓN DE CLICKS (Botones Editar, Cancelar, Guardar)
  list.addEventListener('click', (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    const card = getCard(btn);
    if (!card) return;

    if (btn.classList.contains('edit-btn')) {
      e.preventDefault();
      startEdit(card);
    } else if (btn.classList.contains('cancel-btn')) {
      e.preventDefault();
      cancelEdit(card);
    } else if (btn.classList.contains('save-btn')) {
      e.preventDefault();
      saveEdit(card);
    } else if (btn.classList.contains('delete-btn')) {
      e.preventDefault();
      openDeleteModal(card);
    }
  });

  // BÚSQUEDA
  const searchInput = document.getElementById('search-input');
  const searchBtn = document.getElementById('search-btn');

  const doSearch = () => {
    const q = searchInput?.value?.toLowerCase().trim() || '';
    document.querySelectorAll('.collection-card').forEach(card => {
      const t = card.dataset.titulo?.toLowerCase() || '';
      const d = card.dataset.descripcion?.toLowerCase() || '';
      card.style.display = (t.includes(q) || d.includes(q)) ? '' : 'none';
    });
  };
  searchBtn?.addEventListener('click', (e) => { e.preventDefault(); doSearch(); });
  searchInput?.addEventListener('keyup', (e) => { if (e.key === 'Enter') doSearch(); });

});

const catSelect = document.getElementById('crit-select-category');
const catOtraGroup = document.getElementById('crit-category-otra-group');
const catOtraInput = document.getElementById('crit-input-category-otra');

catSelect?.addEventListener('change', () => {
  if (catSelect.value === 'Otro') {
    catOtraGroup.classList.remove('hidden');
  } else {
    catOtraGroup.classList.add('hidden');
    if (catOtraInput) catOtraInput.value = '';
  }
});
