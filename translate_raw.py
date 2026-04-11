import json
import time
import re
import sys
import urllib.parse
import requests

DATA_DIR = "app/src/main/assets/data"

with open(f"{DATA_DIR}/articles.json", "r", encoding="utf-8") as f:
    articles = json.load(f)

def translate_chunk(text):
    if not text.strip(): return text
    url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=ru&dt=t&q=" + urllib.parse.quote(text)
    
    try:
        response = requests.get(url, headers={'User-Agent': 'Mozilla/5.0'}, timeout=10)
        res = response.json()
        translated_text = "".join([i[0] for i in res[0]])
        return translated_text
    except Exception as e:
        print("Error translating:", e)
        time.sleep(2)
        try:
            response = requests.get(url, headers={'User-Agent': 'Mozilla/5.0'}, timeout=10)
            res = response.json()
            translated_text = "".join([i[0] for i in res[0]])
            return translated_text
        except Exception as e2:
            print("Retry failed:", e2)
            return text

def translate_content(content):
    images = []
    def repl_img(match):
        images.append(match.group(0))
        return f"__IMG_{len(images)-1}__"
        
    content_no_img = re.sub(r'<image:(.*?):(.*?)>', repl_img, content)
    
    paragraphs = content_no_img.split('\n\n')
    translated_paragraphs = []
    
    for p in paragraphs:
        if not p.strip():
            translated_paragraphs.append(p)
            continue
            
        lines = p.split('\n')
        trans_lines = []
        for line in lines:
            if not line.strip():
                trans_lines.append(line)
                continue
                
            is_header = False
            header_level = 0
            if line.startswith('#'):
                header_match = re.match(r'^(#+)\s*(.*)', line)
                if header_match:
                    is_header = True
                    header_level = len(header_match.group(1))
                    line = header_match.group(2)
            
            is_list = False
            if line.startswith('• '):
                is_list = True
                line = line[2:]
                    
            if len(line) > 4000:
                sublines = line.split('. ')
                trans_sublines = [translate_chunk(s) for s in sublines]
                trans_line = '. '.join(trans_sublines)
            else:
                trans_line = translate_chunk(line)
                
            if is_list:
                trans_line = '• ' + trans_line
            if is_header:
                trans_line = '#' * header_level + ' ' + trans_line
                
            trans_lines.append(trans_line)
            sys.stdout.flush()
            
        translated_paragraphs.append('\n'.join(trans_lines))
        
    translated_content = '\n\n'.join(translated_paragraphs)
    
    for i, img in enumerate(images):
        translated_content = translated_content.replace(f"__IMG_{i}__", img)
        
    return translated_content

for i, article in enumerate(articles):
    print(f"Translating article {i+1}/{len(articles)}: {article['title']}")
    sys.stdout.flush()
    ru_count = sum(1 for c in article['content'] if '\u0400' <= c <= '\u04FF')
    total_alpha = sum(1 for c in article['content'] if c.isalpha())
    if total_alpha > 0 and ru_count / total_alpha > 0.1:
        print("Already translated, skipping.")
        sys.stdout.flush()
        continue

    article['content'] = translate_content(article['content'])
    with open(f"{DATA_DIR}/articles.json", "w", encoding="utf-8") as f:
        json.dump(articles, f, ensure_ascii=False, indent=2)

print("Done translating!")
