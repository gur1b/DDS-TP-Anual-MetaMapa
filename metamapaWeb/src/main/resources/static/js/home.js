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
document.querySelector('.report-card')?.addEventListener('submit', e => {
    e.preventDefault();
    const btn = e.currentTarget.querySelector('.btn-primary');
    const original = btn.textContent;
    btn.disabled = true; btn.textContent = 'Enviando...';
    setTimeout(() => { btn.textContent = '¡Gracias por reportar!'; }, 800);
    setTimeout(() => { btn.disabled = false; btn.textContent = original; e.target.reset(); }, 2200);
});

// ATTACHMENT
const ev = document.getElementById('evidencias');
const evName = document.getElementById('evidencias-name');
ev?.addEventListener('change', () => {
  evName.textContent = ev.files?.[0]?.name || '';
});

