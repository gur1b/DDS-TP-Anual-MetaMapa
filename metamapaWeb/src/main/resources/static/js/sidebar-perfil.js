// JS para abrir/cerrar el sidebar de perfil y cargarlo por AJAX solo si el usuario está autenticado

document.addEventListener('DOMContentLoaded', function() {
    const perfilBtn = document.getElementById('btn-sidebar-perfil');
    if (!perfilBtn) return;
    perfilBtn.addEventListener('click', function(e) {
        e.preventDefault();
        abrirSidebarPerfil();
    });
});

function abrirSidebarPerfil() {
    let sidebar = document.getElementById('sidebar-perfil-overlay');
    if (sidebar) {
        sidebar.style.display = 'block';
        return;
    }
    sidebar = document.createElement('div');
    sidebar.id = 'sidebar-perfil-overlay';
    sidebar.innerHTML = `
        <div class="sidebar-perfil-bg"></div>
        <aside class="sidebar-perfil-content"></aside>
    `;
    document.body.appendChild(sidebar);
    sidebar.querySelector('.sidebar-perfil-bg').onclick = cerrarSidebarPerfil;
    fetch('/perfil', {headers: {'X-Requested-With': 'XMLHttpRequest'}})
        .then(r => r.text())
        .then(html => {
            sidebar.querySelector('.sidebar-perfil-content').innerHTML = html;
        });
}

function cerrarSidebarPerfil() {
    let sidebar = document.getElementById('sidebar-perfil-overlay');
    if (sidebar) sidebar.style.display = 'none';
}
