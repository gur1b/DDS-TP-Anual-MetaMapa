// Header sticky + burger
const header = document.querySelector('.site-header');
const onScroll = () => header.classList.toggle('is-scrolled', window.scrollY > 2);
document.addEventListener('scroll', onScroll); onScroll();

const burger = document.querySelector('.burger');
const menu = document.querySelector('.menu');
burger?.addEventListener('click', () => {
  menu.style.display = (menu.style.display === 'flex') ? 'none' : 'flex';
});

const form = document.querySelector('form');
const motivo = document.getElementById('descripcion');
const counter = document.querySelector('.char-count');

// 4. Buscamos el div de error (o creamos uno si no existe en el HTML)
let err = document.getElementById('error');
if (!err) {
  // Si no existe en el HTML, lo creamos dinámicamente para que no falle
  err = document.createElement('div');
  err.id = 'error';
  err.style.color = 'red';
  err.style.fontWeight = 'bold';
  err.style.marginTop = '10px';
  err.hidden = true;
  // Lo insertamos antes de los botones
  const actions = document.querySelector('.form-actions');
  if(actions && form) form.insertBefore(err, actions);
}

// Función de contador
function updateCount(){
  // Actualiza el texto usando la clase .char-count
  if(counter && motivo) {
    counter.textContent = `${motivo.value.length}/1000`;
  }
}

if(motivo) {
  motivo.addEventListener('input', updateCount);
  // Inicializar
  updateCount();
}

// === VALIDACIÓN ===
if(form) {
  form.addEventListener('submit', (e) => {
    // Limpiamos errores previos
    if(err) err.hidden = true;

    // Validamos el campo 'motivo' (que ahora apunta al id="descripcion")
    if(!motivo.value.trim()){
      e.preventDefault(); // Frena el envío

      if(err) {
        err.textContent = "Por favor, escribe un motivo para la eliminación.";
        err.hidden = false;
      } else {
        alert("Por favor, escribe un motivo."); // Fallback por si falla el div
      }

      motivo.focus();
    }
    // Si hay texto, deja pasar el evento y el form se envía solo.
  });
}
