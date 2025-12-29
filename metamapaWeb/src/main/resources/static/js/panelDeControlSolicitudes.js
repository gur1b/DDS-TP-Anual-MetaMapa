// JS simple solo para búsqueda — los formularios hacen submit clásico
document.addEventListener('DOMContentLoaded', () => {
  const searchInput = document.getElementById('search-input');
  const searchBtn = document.getElementById('search-btn');
  const list = document.getElementById('solicitudes-list');

  if (!searchInput || !list) return;

  const filtrar = () => {
    const texto = searchInput.value.toLowerCase().trim();
    const cards = list.querySelectorAll('.solicitud-card');

    cards.forEach(card => {
      const contenido = card.textContent.toLowerCase();
      const coincide = contenido.includes(texto);
      card.style.display = coincide ? '' : 'none';
    });
  };

  searchInput.addEventListener('input', filtrar);
  searchBtn?.addEventListener('click', e => {
    e.preventDefault();
    filtrar();
  });
});
