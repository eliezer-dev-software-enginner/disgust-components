// Lista de arquivos JSON documentados — adicione aqui conforme novos packs
// (InputsPack.json, etc.) forem gerados dentro de jsons/.
const COMPONENT_FILES = ["ButtonsPack.json"];

const nav = document.getElementById("nav");
const content = document.getElementById("content");
const components = {};

async function loadAll() {
    for (const file of COMPONENT_FILES) {
        const res = await fetch(`jsons/${file}`);
        const data = await res.json();
        components[data.component] = data;

        const link = document.createElement("a");
        link.href = `#${data.component}`;
        link.textContent = data.component;
        link.onclick = () => render(data.component);
        nav.appendChild(link);
    }

    const initial = location.hash.replace("#", "") || Object.keys(components)[0];
    render(initial);
}

function render(name) {
    const data = components[name];
    if (!data) return;

    document.querySelectorAll("#nav a").forEach(a =>
        a.classList.toggle("active", a.textContent === name)
    );
    location.hash = name;

    content.innerHTML = `
        <h1>${data.component}</h1>
        <p class="component-description">${data.description}</p>
        ${renderExamples(data)}
        ${renderPropsTable(data)}
        ${renderEnums(data)}
        ${renderConstraints(data)}
        ${renderShortcuts(data)}
    `;
    content.querySelectorAll(".copy-btn").forEach(btn =>
        btn.addEventListener("click", onCopyClick)
    );
}

function renderExamples(data) {
    if (!data.examples?.length) return "";
    return data.examples.map(ex => `
        <h2>${ex.title}</h2>
        <p class="example-description">${ex.description}</p>
        ${ex.preview ? renderPreview(ex.preview, data.palette) : ""}
        ${codeBlock(ex.code)}
    `).join("");
}

function renderPreview(items, palette) {
    const buttons = items.map(item => renderPreviewButton(item, palette)).join("");
    return `<div class="preview-box">${buttons}</div>`;
}

function renderPreviewButton(item, palette) {
    const color = palette[item.variant] || palette.PRIMARY;
    const style = item.style || "FILLED";
    const size = item.size || "MEDIUM";
    const disabled = item.variant === "DISABLED";

    const sizeClass = `btn-size-${size.toLowerCase()}`;
    const styleClass = `btn-style-${style.toLowerCase()}`;
    const widthStyle = item.fillWidth ? "width: 100%; display: block;" : "";

    let colorStyle;
    if (style === "FILLED") {
        colorStyle = `background:${color}; color:${palette.textOnFilled}; border:1px solid ${color};`;
    } else if (style === "OUTLINED") {
        colorStyle = `background:transparent; color:${color}; border:1px solid ${color};`;
    } else {
        colorStyle = `background:transparent; color:${color}; border:1px solid transparent;`;
    }

    const iconTag = item.icon
        ? `<iconify-icon icon="ant-design:${item.icon.name}" class="preview-icon"></iconify-icon>`
        : "";
    const iconStart = item.icon?.position === "start" ? iconTag : "";
    const iconEnd = item.icon?.position === "end" ? iconTag : "";

    return `
        <button class="preview-btn ${sizeClass} ${styleClass}" style="${colorStyle} ${widthStyle}" ${disabled ? "disabled" : ""}>
            ${iconStart}${item.text}${iconEnd}
        </button>
    `;
}

function codeBlock(code) {
    return `
        <div class="code-block">
            <button class="copy-btn">Copiar</button>
            <pre>${escapeHtml(code)}</pre>
        </div>
    `;
}

function renderPropsTable(data) {
    if (!data.props) return "";
    const rows = Object.entries(data.props).map(([name, p]) => `
        <tr>
            <td><code>${name}</code>${p.required ? '<span class="badge badge-required">obrigatório</span>' : ""}</td>
            <td><code>${p.enumRef || p.type}</code></td>
            <td>${p.default !== undefined ? `<code>${p.default}</code>` : "—"}</td>
            <td>${p.description}</td>
        </tr>
    `).join("");

    return `
        <h2>Props</h2>
        <table>
            <thead>
                <tr><th>Nome</th><th>Tipo</th><th>Padrão</th><th>Descrição</th></tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

function renderEnums(data) {
    if (!data.enums) return "";
    return Object.entries(data.enums).map(([enumName, values]) => `
        <h2>${enumName}</h2>
        <div>${values.map(v => `<span class="enum-pill">${v.value}</span>`).join("")}</div>
    `).join("");
}

function renderConstraints(data) {
    if (!data.constraints?.length) return "";
    return `
        <h2>Regras <span class="muted">validadas em runtime</span></h2>
        <ul class="constraints-list">
            ${data.constraints.map(c => `<li>${c}</li>`).join("")}
        </ul>
    `;
}

function renderShortcuts(data) {
    if (!data.shortcuts?.length) return "";
    const items = data.shortcuts.map(s => `
        <div class="shortcut-item">
            <div class="shortcut-name">${s.name}</div>
            ${s.overloads.map(o => `
                <div class="shortcut-overload">(${o.params.join(", ")})</div>
            `).join("")}
        </div>
    `).join("");

    return `<h2>Atalhos estáticos</h2>${items}`;
}

function onCopyClick(e) {
    const code = e.target.nextElementSibling.textContent;
    navigator.clipboard.writeText(code);
    e.target.textContent = "Copiado!";
    setTimeout(() => (e.target.textContent = "Copiar"), 1500);
}

function escapeHtml(str) {
    return str
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

window.addEventListener("hashchange", () => {
    const name = location.hash.replace("#", "");
    if (components[name]) render(name);
});

loadAll();