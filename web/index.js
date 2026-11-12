// Each file documents an existing native Java API. The HTML only illustrates it.
const COMPONENT_FILES = [
    "ButtonsPack.json",
    "Inputs.json",
    "Material.json",
    "Compositions.json",
    "BrazilianInputs.json",
    "Tooltip.json",
    "Icons.json"
];

const nav = document.getElementById("nav");
const content = document.getElementById("content");
const components = {};

async function loadAll() {
    for (const file of COMPONENT_FILES) {
        const res = await fetch(`jsons/${file}`);
        if (!res.ok) throw new Error(`Não foi possível carregar ${file}`);
        const data = await res.json();
        components[data.component] = data;

        const link = document.createElement("a");
        link.href = `#${data.component}`;
        link.textContent = data.component;
        link.onclick = () => render(data.component);
        nav.appendChild(link);
    }

    const initial = location.hash.replace("#", "") || Object.keys(components)[0];
    render(components[initial] ? initial : Object.keys(components)[0]);
}

function renderLoadError(error) {
    content.innerHTML = `<div class="load-error"><strong>Falha ao carregar o catálogo.</strong><span>${escapeHtml(error.message)}</span></div>`;
}

function render(name) {
    const data = components[name];
    if (!data) return;

    document.querySelectorAll("#nav a").forEach(a =>
        a.classList.toggle("active", a.textContent === name)
    );
    location.hash = name;

    content.innerHTML = `
        <div class="documentation-notice">
            <strong>Referência da interface nativa</strong>
            <span>As prévias abaixo ilustram o que é renderizado pela API Java em aplicações Megalodonte. Elas não são componentes web.</span>
        </div>
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
    const controls = items.map(item => item.control
        ? renderPreviewInput(item)
        : renderPreviewButton(item, palette)).join("");
    return `<div class="preview-box">${controls}</div>`;
}

function renderPreviewInput(item) {
    const id = `preview-${item.control}-${item.label || item.placeholder || "field"}`
        .toLowerCase().replace(/[^a-z0-9-]+/g, "-");
    const label = item.label ? `<label for="${id}">${item.label}</label>` : "";
    const value = escapeAttribute(item.value || "");
    const placeholder = escapeAttribute(item.placeholder || "");

    let control;
    switch (item.control) {
        case "alert":
            return `<div class="preview-alert preview-${item.severity || "info"}"><strong>${escapeHtml((item.severity || "info").toUpperCase())}</strong><span>${escapeHtml(item.text)}</span></div>`;
        case "chip":
            return `<span class="preview-chip preview-${item.severity || "info"}">${escapeHtml(item.text)}</span>`;
        case "avatar":
            return `<span class="preview-avatar">${escapeHtml(item.text)}</span>`;
        case "badge":
            return `<span class="preview-badge-wrap"><span class="preview-avatar">${escapeHtml(item.text || "UI")}</span><span class="preview-badge">${escapeHtml(String(item.count))}</span></span>`;
        case "breadcrumbs":
            return `<nav class="preview-breadcrumbs">${(item.items || []).map((part, index, all) => `<span class="${index === all.length - 1 ? "current" : ""}">${escapeHtml(part)}</span>`).join("<b>/</b>")}</nav>`;
        case "divider":
            return `<hr class="preview-divider">`;
        case "paper":
            return `<div class="preview-paper"><strong>${escapeHtml(item.title)}</strong><span>${escapeHtml(item.text || "")}</span></div>`;
        case "dialog":
            return `<div class="preview-dialog"><strong>${escapeHtml(item.title)}</strong><span>${escapeHtml(item.text)}</span><button>Fechar</button></div>`;
        case "snackbar":
            return `<div class="preview-snackbar">${escapeHtml(item.text)}</div>`;
        case "accordion":
            return `<details class="preview-accordion" open><summary>${escapeHtml(item.title)}</summary><p>${escapeHtml(item.text)}</p></details>`;
        case "tabs":
            return `<div class="preview-tabs">${(item.items || []).map((part, index) => `<span class="${index === (item.selected || 0) ? "active" : ""}">${escapeHtml(part)}</span>`).join("")}</div>`;
        case "progress":
            return `<div class="preview-progress"><span style="width:${Number(item.value) || 0}%"></span><b>${Number(item.value) || 0}%</b></div>`;
        case "tooltip":
            return `<span class="preview-tooltip"><button>${escapeHtml(item.label)}</button><span>${escapeHtml(item.text)}</span></span>`;
        case "details":
            return `<div class="preview-details"><strong>${escapeHtml(item.label)}</strong><span>${escapeHtml(item.value)}</span></div>`;
        case "title":
            return `<div class="preview-title"><strong>${escapeHtml(item.text)}</strong><span>${escapeHtml(item.subtitle || "")}</span></div>`;
        case "image-selector":
            return `<div class="preview-image-selector"><div>▧</div><strong>${escapeHtml(item.label)}</strong><button>Selecionar imagem</button></div>`;
        case "menu-item":
            return `<div class="preview-menu-item"><iconify-icon icon="feather:${item.icon || "box"}"></iconify-icon><span>${escapeHtml(item.label)}</span></div>`;
        case "icon-grid":
            return `<div class="preview-icon-grid">${(item.items || []).map(icon => `<div><iconify-icon icon="feather:${icon.name}"></iconify-icon><span>${escapeHtml(icon.label)}</span></div>`).join("")}</div>`;
        case "password":
            control = `<input id="${id}" type="password" value="${value}" placeholder="${placeholder}">`;
            break;
        case "textarea":
            control = `<textarea id="${id}" placeholder="${placeholder}" rows="4">${escapeHtml(item.value || "")}</textarea>`;
            break;
        case "select":
            control = `<select id="${id}">${(item.options || []).map(option =>
                `<option${option === item.value ? " selected" : ""}>${escapeHtml(option)}</option>`).join("")}</select>`;
            break;
        case "checkbox":
            return `<label class="preview-check"><input type="checkbox" ${item.checked ? "checked" : ""}> ${escapeHtml(item.label)}</label>`;
        case "switch":
            return `<label class="preview-switch"><input type="checkbox" ${item.checked ? "checked" : ""}><span></span>${escapeHtml(item.label)}</label>`;
        case "radio":
            return `<fieldset class="preview-radio"><legend>${escapeHtml(item.label)}</legend>${(item.options || []).map((option, index) =>
                `<label><input type="radio" name="${id}" ${index === 0 ? "checked" : ""}> ${escapeHtml(option)}</label>`).join("")}</fieldset>`;
        case "range":
            control = `<input id="${id}" type="range" min="${item.min}" max="${item.max}" value="${item.value}">`;
            break;
        case "date":
            control = `<input id="${id}" type="date" value="${value}">`;
            break;
        default:
            control = `<input id="${id}" type="text" value="${value}" placeholder="${placeholder}">`;
    }
    return `<div class="preview-field ${item.wide ? "preview-field-wide" : ""}">${label}${control}</div>`;
}

function renderPreviewButton(item, palette) {
    const colors = palette || {
        PRIMARY: "#2563eb", SECONDARY: "#64748b", SUCCESS: "#10b981",
        WARNING: "#f59e0b", DANGER: "#ef4444", DISABLED: "#64748b",
        textOnFilled: "#ffffff"
    };
    const color = colors[item.variant] || colors.PRIMARY;
    const style = item.style || "FILLED";
    const size = item.size || "MEDIUM";
    const disabled = item.variant === "DISABLED";

    const sizeClass = `btn-size-${size.toLowerCase()}`;
    const styleClass = `btn-style-${style.toLowerCase()}`;
    const widthStyle = item.fillWidth ? "width: 100%; display: block;" : "";

    let colorStyle;
    if (style === "FILLED") {
        colorStyle = `background:${color}; color:${colors.textOnFilled}; border:1px solid ${color};`;
    } else if (style === "OUTLINED") {
        colorStyle = `background:transparent; color:${color}; border:1px solid ${color};`;
    } else {
        colorStyle = `background:transparent; color:${color}; border:1px solid transparent;`;
    }

    const iconTag = item.icon
        ? `<iconify-icon icon="feather:${item.icon.name}" class="preview-icon"></iconify-icon>`
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
            <pre><code class="language-java">${highlightJava(code)}</code></pre>
        </div>
    `;
}

function highlightJava(code) {
    const tokens = [];
    const protectedCode = String(code).replace(
        /(\/\/[^\n]*|\/\*[\s\S]*?\*\/|"(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*')/g,
        token => {
            const kind = token.startsWith("//") || token.startsWith("/*") ? "comment" : "string";
            tokens.push(`<span class="token-${kind}">${escapeHtml(token)}</span>`);
            return String.fromCodePoint(0xE000 + tokens.length - 1);
        }
    );

    let highlighted = escapeHtml(protectedCode)
        .replace(/\b(var|new|return|public|private|protected|static|final|class|record|enum|extends|implements|import|package|true|false|null|this)\b/g,
            '<span class="token-keyword">$1</span>')
        .replace(/\b(State|String|InputProps|InputsPack|Material|Severity|Pack|Tooltip|Feather|Icon|Component|List|LocalDate|Double|Boolean|RadioGroup|Option|Tabs|Page|Color)\b/g,
            '<span class="token-type">$1</span>')
        .replace(/\b([a-zA-Z_$][\w$]*)(?=\()/g, '<span class="token-method">$1</span>')
        .replace(/\b(\d+(?:\.\d+)?)\b/g, '<span class="token-number">$1</span>');

    return highlighted.replace(/[\uE000-\uF8FF]/g,
        marker => tokens[marker.codePointAt(0) - 0xE000]);
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

async function onCopyClick(e) {
    const button = e.currentTarget;
    const code = button.nextElementSibling.textContent;
    button.disabled = true;
    try {
        await navigator.clipboard.writeText(code);
        button.textContent = "Copiado!";
    } catch {
        button.textContent = "Não foi possível copiar";
    } finally {
        setTimeout(() => {
            button.textContent = "Copiar";
            button.disabled = false;
        }, 1500);
    }
}

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

function escapeAttribute(str) {
    return escapeHtml(String(str)).replace(/"/g, "&quot;");
}

window.addEventListener("hashchange", () => {
    const name = location.hash.replace("#", "");
    if (components[name]) render(name);
});

loadAll().catch(renderLoadError);
