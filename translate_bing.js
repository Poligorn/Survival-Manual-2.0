const fs = require('fs');
const { translate } = require('bing-translate-api');

const DATA_DIR = "app/src/main/assets/data";
let articles = JSON.parse(fs.readFileSync(`${DATA_DIR}/articles.json`, 'utf-8'));

async function translateChunk(text) {
    if (!text.trim()) return text;
    try {
        const res = await translate(text, null, 'ru');
        return res.translation;
    } catch (e) {
        console.log("Error translating:", e.message);
        await new Promise(r => setTimeout(r, 2000));
        try {
            const res = await translate(text, null, 'ru');
            return res.translation;
        } catch (e2) {
            console.log("Retry failed:", e2.message);
            return text;
        }
    }
}

async function translateContent(content) {
    let images = [];
    const contentNoImg = content.replace(/<image:(.*?):(.[^>]*)>/g, (match) => {
        images.push(match);
        return `__IMG_${images.length - 1}__`;
    });

    const paragraphs = contentNoImg.split('\n\n');
    const translatedParagraphs = [];

    for (let p of paragraphs) {
        if (!p.trim()) {
            translatedParagraphs.push(p);
            continue;
        }

        const lines = p.split('\n');
        const transLines = [];
        for (let line of lines) {
            if (!line.trim()) {
                transLines.push(line);
                continue;
            }

            let isHeader = false;
            let headerLevel = 0;
            if (line.startsWith('#')) {
                const headerMatch = line.match(/^(#+)\s*(.*)/);
                if (headerMatch) {
                    isHeader = true;
                    headerLevel = headerMatch[1].length;
                    line = headerMatch[2];
                }
            }

            let isList = false;
            if (line.startsWith('• ')) {
                isList = true;
                line = line.substring(2);
            }

            let transLine;
            if (line.length > 4000) {
                const sublines = line.split('. ');
                const transSublines = [];
                for (let s of sublines) {
                    transSublines.push(await translateChunk(s));
                }
                transLine = transSublines.join('. ');
            } else {
                transLine = await translateChunk(line);
            }

            if (isList) transLine = '• ' + transLine;
            if (isHeader) transLine = '#'.repeat(headerLevel) + ' ' + transLine;

            transLines.push(transLine);
        }

        translatedParagraphs.push(transLines.join('\n'));
    }

    let translatedContent = translatedParagraphs.join('\n\n');

    for (let i = 0; i < images.length; i++) {
        translatedContent = translatedContent.replace(`__IMG_${i}__`, images[i]);
    }

    return translatedContent;
}

async function main() {
    for (let i = 0; i < articles.length; i++) {
        const article = articles[i];
        console.log(`Translating article ${i + 1}/${articles.length}: ${article.title}`);

        const ruCount = (article.content.match(/[А-Яа-яЁё]/g) || []).length;
        const totalAlpha = (article.content.match(/[a-zA-ZА-Яа-яЁё]/g) || []).length;
        if (totalAlpha > 0 && ruCount / totalAlpha > 0.1) {
            console.log("Already translated, skipping.");
            continue;
        }

        article.content = await translateContent(article.content);
        fs.writeFileSync(`${DATA_DIR}/articles.json`, JSON.stringify(articles, null, 2));
    }
    console.log("Done translating!");
}

main().catch(console.error);