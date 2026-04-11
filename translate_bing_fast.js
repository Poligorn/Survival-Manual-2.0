const fs = require('fs');
const { translate } = require('bing-translate-api');

const DATA_DIR = "app/src/main/assets/data";
let articles = JSON.parse(fs.readFileSync(`${DATA_DIR}/articles.json`, 'utf-8'));

async function translateChunk(text) {
    if (!text.trim()) return text;
    let retries = 0;
    while(true) {
        try {
            const res = await translate(text, null, 'ru');
            if (res && res.translation) {
                return res.translation;
            }
            throw new Error("Empty translation result");
        } catch (e) {
            retries++;
            console.log(`Error translating chunk "${text.substring(0, 20)}..." (attempt ${retries}):`, e.message);
            await new Promise(r => setTimeout(r, Math.min(10000, 2000 + retries * 1000)));
        }
    }
}

async function processArrayInChunks(array, fn, chunkSize = 5) {
    const results = [];
    for (let i = 0; i < array.length; i += chunkSize) {
        const chunk = array.slice(i, i + chunkSize);
        const chunkResults = await Promise.all(chunk.map(fn));
        results.push(...chunkResults);
    }
    return results;
}

async function translateContent(content) {
    let images = [];
    const contentNoImg = content.replace(/<image:(.*?):(.[^>]*)>/g, (match) => {
        images.push(match);
        return `__IMG_${images.length - 1}__`;
    });

    const paragraphs = contentNoImg.split('\n\n');
    
    const translateParagraph = async (p) => {
        if (!p.trim()) return p;
        const lines = p.split('\n');
        
        const translateLine = async (line) => {
            if (!line.trim()) return line;
            
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
            if (line.length > 3000) {
                const sublines = line.split('. ');
                const transSublines = await processArrayInChunks(sublines, translateChunk, 3);
                transLine = transSublines.join('. ');
            } else {
                transLine = await translateChunk(line);
            }

            if (isList) transLine = '• ' + transLine;
            if (isHeader) transLine = '#'.repeat(headerLevel) + ' ' + transLine;

            return transLine;
        };
        
        const transLines = await processArrayInChunks(lines, translateLine, 3);
        return transLines.join('\n');
    };

    const translatedParagraphs = await processArrayInChunks(paragraphs, translateParagraph, 2);

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
        if (totalAlpha > 0 && ruCount / totalAlpha > 0.4) { // Check if at least 40% are Cyrillic
            console.log("Already translated, skipping.");
            continue;
        }

        console.log(`Needs translation...`);
        article.content = await translateContent(article.content);
        fs.writeFileSync(`${DATA_DIR}/articles.json`, JSON.stringify(articles, null, 2));
        console.log(`Saved article ${i + 1}`);
    }
    console.log("Done translating!");
}

main().catch(console.error);