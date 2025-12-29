const API_BASE = "/dashboard-api"; // Ahora apunta a tu propio backend web

// =============== KPIs y combinadas ===============
Promise.all([
    fetch(`${API_BASE}/provincia-con-mas-hechos`).then(r=>r.json()),
    fetch(`${API_BASE}/categoria-mayor-cantidad`).then(r=>r.json()),
    fetch(`${API_BASE}/cantidad-spam`).then(r=>r.json())
]).then(([provincias, categorias, spam]) => {
    document.getElementById('provincia-top').innerHTML =
        `<b>Provincia con más hechos</b><br>${provincias[0]?.provincia ?? 'N/A'}: <b>${provincias[0]?.cantidad ?? '—'}</b>`;
    document.getElementById('categoria-top').innerHTML =
        `<b>Categoría más reportada</b><br>${categorias[0]?.categoria ?? 'N/A'}: <b>${categorias[0]?.cantidad ?? '—'}</b>`;
    document.getElementById('solicitudes-top').innerHTML =
        `<b>Solicitudes de eliminación aceptadas</b><br>${spam["solicitudes spam"]} / ${spam["total de solicitudes"]}`;
});


// =============== Gráficos principales ===============
fetch(`${API_BASE}/provincia-con-mas-hechos`).then(r=>r.json()).then(provincias => {
    new Chart(document.getElementById('hechos-por-provincia'), {
        type: 'bar',
        data: {
            labels: provincias.map(p => p.provincia),
            datasets: [{
                label: 'Hechos',
                data: provincias.map(p=>p.cantidad),
                backgroundColor: [
                    '#2B76B9', '#23BFA9', '#77C5D5', '#FFB85F', '#82C0E2', '#146886', '#DF7373', '#E5F3FA'
                ],
                borderRadius: 12,
                borderSkipped: false,
            }]
        },
        options: {
            responsive:true,
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: {
                    ticks: {
                        color: "#2B76B9",
                        font: { weight: 'bold' }
                    },
                    grid: { display: false }
                },
                y: {
                    beginAtZero: true,
                    ticks: {
                        color: "#146886",
                        stepSize: 1
                    },
                    grid: { color: "#F2FAFC" }
                }
            }
        }
    });
});

fetch(`${API_BASE}/categoria-mayor-cantidad`).then(r=>r.json()).then(categorias => {
    new Chart(document.getElementById('hechos-por-categoria'), {
        type: 'doughnut',
        data: {
            labels: categorias.map(c => c.categoria),
            datasets: [{
                label: 'Hechos',
                data: categorias.map(c => c.cantidad),
                backgroundColor: [
                    '#2B76B9', '#146886', '#82C0E2', '#E5F3FA',
                    '#FFB85F', '#23BFA9', '#FD7C6E', '#DF7373', '#77C5D5'
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'Hechos por categoría'
                },
                datalabels: {
                    color: '#333',
                    font: {
                        weight: 'bold'
                    },
                    formatter: (value, context) => value
                }
            }
        }
    });
});

// ========== Filtros por categoría ===========
const filtroSel = document.getElementById('categoria-filtro');
fetch(`${API_BASE}/categoria-mayor-cantidad`).then(r=>r.json()).then(categorias => {
    categorias.forEach(cat => {
        let option = document.createElement('option');
        option.value = cat.categoria;
        option.textContent = cat.categoria;
        filtroSel.appendChild(option);
    });
});

let chartProvCat = null, chartHoraCat = null;
filtroSel.addEventListener('change', function() {
    const cat = filtroSel.value;
    if (!cat) return;
    // Links de descarga (estos pueden ir directo al backend de estadísticas si no hacés proxy para descargas)
    document.getElementById('csv-prov-categ').href = `http://localhost:8090/servicioEstadisticas/export/csv/provincia-por-categoria/${encodeURIComponent(cat)}`;
    document.getElementById('csv-hora-categ').href = `http://localhost:8090/servicioEstadisticas/export/csv/horario-por-categoria/${encodeURIComponent(cat)}`;
    // Gráficos por categoría seleccionada
    fetch(`${API_BASE}/provincia-con-mas-hechos-por-categoria?categoria=${encodeURIComponent(cat)}`)
        .then(r=>r.json()).then(data => {
        if(chartProvCat) chartProvCat.destroy();
        chartProvCat = new Chart(document.getElementById('hechos-por-provincia-cat'), {
            type:'bar',
            data: {
                labels: data.map(x=>x.provincia),
                datasets: [{
                    label:'Hechos',
                    data: data.map(x=>x.cantidad),
                    backgroundColor: '#23BFA9'
                }]
            },
            options:{responsive:true, plugins:{legend:{display:false}}}
        });
    });
    fetch(`${API_BASE}/horario-por-categoria?categoria=${encodeURIComponent(cat)}`)
        .then(r=>r.json()).then(data => {
        if(chartHoraCat) chartHoraCat.destroy();
        chartHoraCat = new Chart(document.getElementById('hechos-por-hora-cat'), {
            type:'bar',
            data: {
                labels: data.map(x=>x.hora),
                datasets: [{
                    label:'Hechos',
                    data: data.map(x=>x.cantidad),
                    backgroundColor:'#FFB85F'
                }]
            },
            options:{responsive:true, plugins:{legend:{display:false}}}
        });
    });
});
// Ejemplo de labels típicos
const provinciasLabels = [""];
const horasLabels = [""];

// Iniciar gráficos con labels y data en cero:
function initEmptyCategoryCharts() {
    if (chartProvCat) chartProvCat.destroy();
    chartProvCat = new Chart(document.getElementById('hechos-por-provincia-cat'), {
        type: 'bar',
        data: {
            labels: provinciasLabels,
            datasets: [{
                label: 'Hechos',
                data: provinciasLabels.map(_ => 0), // barras en 0
                backgroundColor: '#23BFA9'
            }]
        },
        options:{responsive:true, plugins:{legend:{display:false}}}
    });

    if (chartHoraCat) chartHoraCat.destroy();
    chartHoraCat = new Chart(document.getElementById('hechos-por-hora-cat'), {
        type:'bar',
        data: {
            labels: horasLabels,
            datasets: [{
                label:'Hechos',
                data: horasLabels.map(_ => 0),
                backgroundColor:'#FFB85F'
            }]
        },
        options:{responsive:true, plugins:{legend:{display:false}}}
    });
}

// Llamar al cargar la página
initEmptyCategoryCharts();