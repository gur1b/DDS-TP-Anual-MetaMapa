// panelDeControlSugerencias.js
document.addEventListener("DOMContentLoaded", () => {
    const input = document.getElementById("search-input");
    const btn   = document.getElementById("search-btn");
    const list  = document.getElementById("sugerencias-list");

    if (!list) return;

    // Toolbar simple (contador)
    const toolbar = document.createElement("div");
    toolbar.className = "suggest-toolbar";
    toolbar.innerHTML = `<div class="suggest-counter" id="suggest-counter"></div>`;
    list.parentElement.insertBefore(toolbar, list);

    const counter = document.getElementById("suggest-counter");
    const cards   = Array.from(list.querySelectorAll(".solicitud-card"));

    // Indexar texto buscable (una vez)
    cards.forEach(card => {
        const text = card.innerText.toLowerCase().replace(/\s+/g, " ").trim();
        card.dataset.searchText = text;
    });

    const normalize = (q) => (q || "").toLowerCase().trim().replace(/\s+/g, " ");

    function applySearch() {
        const q = normalize(input?.value);
        let visible = 0;

        cards.forEach(card => {
            const hayMatch = !q || (card.dataset.searchText || "").includes(q);
            card.style.display = hayMatch ? "" : "none";
            if (hayMatch) visible++;
        });

        if (counter) counter.textContent = `${visible} / ${cards.length} visibles`;
    }

    // Enter para buscar
    input?.addEventListener("keydown", (e) => {
        if (e.key === "Enter") applySearch();
    });

    // Botón lupa
    btn?.addEventListener("click", applySearch);

    // Búsqueda “live”
    input?.addEventListener("input", applySearch);

    // Primer render
    applySearch();
});
