// Sticky sombra al scrollear
const header = document.querySelector('.site-header');
const onScroll = () => header.classList.toggle('is-scrolled', window.scrollY > 2);
document.addEventListener('scroll', onScroll); onScroll();

// Burger simple (abre/cierra menú en mobile)
const burger = document.querySelector('.burger');
const menu = document.querySelector('.menu');
burger?.addEventListener('click', () => {
    menu.style.display = (menu.style.display === 'flex') ? 'none' : 'flex';
});

// Scroll suave para anclas internas
document.querySelectorAll('a[href^="#"]').forEach(a => {
    a.addEventListener('click', e => {
        const id = a.getAttribute('href');
        const el = document.querySelector(id);
        if (el){
            e.preventDefault();
            el.scrollIntoView({ behavior:'smooth', block:'start' });
        }
    });
});


// Micro-animación al enviar (demo)
// Esto solo se activará en la página que tenga un .report-card
document.querySelector('.report-card')?.addEventListener('submit', e => {
    e.preventDefault();
    const btn = e.currentTarget.querySelector('.btn-primary');
    const original = btn.textContent;
    btn.disabled = true; btn.textContent = 'Enviando...';
    setTimeout(() => { btn.textContent = '¡Gracias por reportar!'; }, 800);
    setTimeout(() => { btn.disabled = false; btn.textContent = original; e.target.reset(); }, 2200);
});

// Utilidad para fetch con manejo simple de errores
async function fetchJSON(url) {
    const res = await fetch(url);
    if (!res.ok) {
        const text = await res.text().catch(() => '');
        throw new Error(`Error ${res.status} en ${url} - ${text}`);
    }
    return res.json();
}

// Cargar todas las colecciones y pintarlas en la sidebar
async function loadColecciones() {
    try {
        const colecciones = await fetchJSON(`${API_BASE}/colecciones`);
        console.log('Colecciones:', colecciones); // Mirá el shape y ajustá campos si hace falta
        renderColecciones(colecciones);
    } catch (err) {
        console.error(err);
        const list = document.querySelector('.collection-list');
        if (list) list.innerHTML = '<p style="color:#c00">No se pudieron cargar las colecciones.</p>';
    }
}