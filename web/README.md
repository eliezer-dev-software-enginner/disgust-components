# Catálogo visual do Disgust

Esta pasta é a documentação estática dos componentes Java nativos do Disgust.
Ela mostra a API disponível e uma aproximação visual do que o usuário enxerga
quando esses componentes são renderizados em aplicações desktop Megalodonte.

O conteúdo daqui **não é uma biblioteca web**:

- não há componentes para navegador, React ou JavaScript;
- não há módulo Gradle `:web` nem artefato Maven `disgust-web`;
- `index.html`, `index.js` e `style.css` servem apenas ao catálogo;
- os arquivos em `jsons/` documentam as classes Java reais;
- as prévias HTML são ilustrações e não definem o comportamento ou a aparência
  final dos controles nativos.

A fonte de verdade de cada componente é sua implementação Java em `src/main` e,
quando o controle pertence ao framework, em `megalodonte-components`. O catálogo
deve usar os nomes, tipos, restrições e exemplos dessas APIs, sem criar uma
segunda hierarquia de componentes.

## Visualização local

Como o catálogo carrega os JSONs com `fetch`, abra-o por um servidor HTTP local:

```bash
python3 -m http.server 8000 --directory web
```

Depois acesse `http://localhost:8000`. Esse servidor é apenas uma forma de ler a
documentação; ele não participa da execução das aplicações Java.

## Como documentar um componente

1. Implemente ou altere primeiro o componente Java nativo.
2. Atualize o JSON correspondente com o pacote, propriedades e exemplos reais.
3. Use a prévia somente para comunicar a aparência observada na aplicação.
4. Deixe explícitas diferenças que dependam do tema, JavaFX ou sistema operacional.
5. Não adicione fachadas Java sob um pacote `disgust.web`.
