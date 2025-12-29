// Utilidad: parsea lat/lon que vienen como String (posible coma decimal)
const toNum = v => {
    if (v == null) return null;
    const n = parseFloat(String(v).replace(',', '.'));
    return Number.isFinite(n) ? n : null;
};

const puntos = hechos
    .map(h => ({
        id: h.id,
        nombre: h.nombre,
        hash: h.hash,
        lat: toNum(h.latitud),
        lon: toNum(h.longitud),
        fecha: h.fechaSuceso,
        hora: h.horaSuceso,
        descripcion: h.descripcion,
        etiquetas: h.etiquetas,
        categorias: h.categorias
    }))
    .filter(p => p.lat != null && p.lon != null);

// Iniciar mapa: default CABA si no hay puntos
const defaultCenter = [-34.6037, -58.3816], defaultZoom = 11;
const map = L.map('mapa', { maxZoom: 19 })
    .setView(
        puntos.length ? [puntos[0].lat, puntos[0].lon] : defaultCenter,
        puntos.length ? 13 : defaultZoom
    );

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors',
    maxZoom: 19
}).addTo(map);

// === NUEVO: grupo de clusters ===
const markerCluster = L.markerClusterGroup({
    // radio de agrupación en px (podés tunearlo)
    maxClusterRadius: 40,
    // si querés que siempre "abra" los puntos cuando hacés zoom
    spiderfyOnEveryZoom: true,
    showCoverageOnHover: false
});

const markers = [];

for (const p of puntos) {
    // Categorías
    let categoriasHtml = '';
    if (p.categorias && p.categorias.length > 0) {
        categoriasHtml = `
            <p><strong>Categorías:</strong></p>
            <div class="tags">
                ${p.categorias.map(cat => `<span class="tag tag-category">${cat}</span>`).join('')}
            </div>
        `;
    }

    // Etiquetas
    let etiquetasHtml = '';
    if (p.etiquetas && p.etiquetas.length > 0) {
        etiquetasHtml = `
            <p><strong>Etiquetas:</strong></p>
            <div class="tags">
                ${p.etiquetas.map(tag => `<span class="tag tag-label">${tag}</span>`).join('')}
            </div>
        `;
    }

    // HTML del popup
    const html = `
      <div class="custom-popup-card">
          <header class="popup-header">${p.nombre ?? '(Sin título)'}</header>
          <section class="popup-body">
              ${categoriasHtml || `<p><strong>Categoría:</strong> No especificada</p>`}
              <p class="description-box">${p.descripcion ?? 'No hay descripción disponible.'}</p>
              <p><strong>Fecha de suceso:</strong> ${p.fecha ?? 'No especificada'}</p>
              ${etiquetasHtml}
             <a class="btn-solicitud" href="/solicitudEliminacion/${p.id}" style="color: white;"> Solicitar eliminación </a>
          </section>
      </div>
    `;

    const marker = L.marker([p.lat, p.lon]).bindPopup(html);

    markers.push(marker);
    markerCluster.addLayer(marker); // en vez de addTo(map)
}

// Agregamos el grupo al mapa
map.addLayer(markerCluster);

// Ajustar el mapa a todos los puntos
if (markers.length > 1) {
    const group = L.featureGroup(markers);
    map.fitBounds(group.getBounds().pad(0.15));
}

window.addEventListener('load', () => map.invalidateSize());
window.addEventListener('resize', () => map.invalidateSize());

// === DETECTOR DE MENSAJES FLASH ===
document.addEventListener("DOMContentLoaded", function() {
    const urlParams = new URLSearchParams(window.location.search);

    // Si la URL tiene ?solicitudEliminacion=ok
    if (urlParams.get('solicitudEliminacion') === 'ok') {
        mostrarToast("¡Solicitud enviada con éxito!", "success");
        limpiarUrl();
    }
    // Si la URL tiene ?solicitudEliminacion=error
    else if (urlParams.get('solicitudEliminacion') === 'error') {
        mostrarToast("Hubo un error al enviar la solicitud.", "error");
        limpiarUrl();
    }
});

function limpiarUrl() {
    // Quita los parámetros feos de la URL sin recargar
    const nuevaUrl = window.location.pathname;
    window.history.replaceState({}, document.title, nuevaUrl);
}

function mostrarToast(mensaje, tipo) {
    const toast = document.createElement("div");
    toast.textContent = mensaje;

    // Color según tipo
    const bgColor = tipo === 'success' ? '#28a745' : '#dc3545'; // Verde o Rojo

    toast.style.cssText = `
        position: fixed;
        /* AJUSTA ESTE VALOR según la altura de tu navbar (aprox 80px suele estar bien) */
        top: 80px; 
        
        /* Esto centra el elemento horizontalmente */
        left: 50%;
        transform: translateX(-50%);
        
        background-color: ${bgColor};
        color: white;
        padding: 15px 30px;
        border-radius: 50px; /* Bordes más redondeados quedan mejor al centro */
        box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        z-index: 9999;
        font-family: sans-serif;
        font-size: 15px;
        font-weight: 500;
        text-align: center;
        opacity: 0;
        transition: opacity 0.5s ease-in-out, top 0.5s ease-in-out;
    `;

    document.body.appendChild(toast);

    // Animación de entrada (hacemos que baje un poquito al aparecer)
    setTimeout(() => {
        toast.style.opacity = "1";
        toast.style.top = "90px"; // Efecto de bajada suave
    }, 100);

    // Animación de salida
    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.top = "80px"; // Vuelve a subir al desaparecer
        setTimeout(() => { document.body.removeChild(toast); }, 500);
    }, 4000);
}