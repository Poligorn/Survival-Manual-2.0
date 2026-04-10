import os
import json
import re

WIKI_DIR = "wiki_tmp"
DATA_DIR = "app/src/main/assets/data"

CATEGORIES = [
    {"id": 1, "title": "Психология и подготовка", "description": "Основы психологической подготовки к экстремальным ситуациям.", "iconResName": "ic_psychology"},
    {"id": 2, "title": "Базовые потребности", "description": "Вода, огонь, укрытие и медицина.", "iconResName": "ic_fire"},
    {"id": 3, "title": "Навигация и сигналы", "description": "Ориентирование на местности и сигналы спасения.", "iconResName": "ic_psychology"},
    {"id": 4, "title": "Растения и животные", "description": "Опасные и полезные растения, насекомые и животные.", "iconResName": "ic_water"},
    {"id": 5, "title": "Специфические условия", "description": "Пустыня, тропики, море и холодный климат.", "iconResName": "ic_shelter"},
    {"id": 6, "title": "Инструменты и узлы", "description": "Веревки, оружие, инструменты и транспорт.", "iconResName": "ic_fire"}
]

ARTICLE_MAP = {
    "Psychology.md": {"title": "Психология выживания", "cat_id": 1, "tags": "#психология,#выживание"},
    "Planning.md": {"title": "Планирование", "cat_id": 1, "tags": "#планирование,#подготовка"},
    "Kits.md": {"title": "Наборы выживания", "cat_id": 1, "tags": "#наборы,#edc"},
    "Medicine.md": {"title": "Базовая медицина", "cat_id": 2, "tags": "#первая_помощь,#медицина"},
    "Shelter.md": {"title": "Укрытия", "cat_id": 2, "tags": "#укрытие,#выживание"},
    "Water.md": {"title": "Добыча воды", "cat_id": 2, "tags": "#вода,#выживание"},
    "Fire.md": {"title": "Разведение огня", "cat_id": 2, "tags": "#разведение_огня,#огонь"},
    "Food.md": {"title": "Добыча пищи", "cat_id": 2, "tags": "#пища,#охота,#рыбалка"},
    "Plants.md": {"title": "Растения для выживания", "cat_id": 4, "tags": "#растения,#еда"},
    "DangerousArthropods.md": {"title": "Опасные насекомые", "cat_id": 4, "tags": "#насекомые,#опасность"},
    "Animals.md": {"title": "Опасные животные", "cat_id": 4, "tags": "#животные,#опасность"},
    "Poisonous-Plants.md": {"title": "Ядовитые растения", "cat_id": 4, "tags": "#растения,#яд"},
    "Tools.md": {"title": "Инструменты и снаряжение", "cat_id": 6, "tags": "#инструменты,#снаряжение"},
    "Weapons.md": {"title": "Оружие", "cat_id": 6, "tags": "#оружие,#инструменты"},
    "RopesAndKnots.md": {"title": "Веревки и узлы", "cat_id": 6, "tags": "#узлы,#веревка"},
    "Desert.md": {"title": "Выживание в пустыне", "cat_id": 5, "tags": "#пустыня,#жара"},
    "Tropical.md": {"title": "Выживание в тропиках", "cat_id": 5, "tags": "#тропики,#джунгли"},
    "Cold.md": {"title": "Холодный климат", "cat_id": 5, "tags": "#холод,#снег"},
    "Sea.md": {"title": "Выживание на море", "cat_id": 5, "tags": "#море,#вода"},
    "DirectionFinding.md": {"title": "Ориентирование", "cat_id": 3, "tags": "#навигация,#ориентирование"},
    "Signaling.md": {"title": "Сигналы спасения", "cat_id": 3, "tags": "#сигналы,#спасение"},
    "WaterCrossing.md": {"title": "Переправа через воду", "cat_id": 5, "tags": "#переправа,#река"},
    "HostileAreas.md": {"title": "Враждебные территории", "cat_id": 1, "tags": "#враждебность,#маскировка"},
    "Camouflage.md": {"title": "Маскировка", "cat_id": 1, "tags": "#маскировка,#скрытность"}
}

articles = []
article_id = 1

def process_markdown(content):
    # Remove GitHub Wiki specific header lines like:
    # Jump to bottom [Edit]...
    # Name edited this page...
    lines = content.split('\n')
    cleaned_lines = []
    skip = False
    for line in lines:
        if line.startswith("Jump to bottom"):
            continue
        if "edited this page" in line and "revisions" in line:
            continue
        # Replace ![Alt text](image.png) with <image:image.png:Alt text>
        # Note: some images might not be in the repo or format might differ
        
        # Regex to find standard markdown images
        # format: ![alt](url)
        img_match = re.search(r'!\[(.*?)\]\((.*?)\)', line)
        if img_match:
            alt_text = img_match.group(1).replace("'", "").replace('"', "")
            img_url = img_match.group(2)
            # Just get filename
            filename = os.path.basename(img_url)
            line = f"<image:{filename}:{alt_text}>"

        cleaned_lines.append(line)
        
    return '\n'.join(cleaned_lines).strip()

for filename in os.listdir(WIKI_DIR):
    if filename.endswith(".md") and filename in ARTICLE_MAP:
        with open(os.path.join(WIKI_DIR, filename), 'r', encoding='utf-8') as f:
            content = f.read()
            
        cleaned_content = process_markdown(content)
        
        meta = ARTICLE_MAP[filename]
        
        icon = "ic_fire"
        if meta['cat_id'] == 1: icon = "ic_psychology"
        elif meta['cat_id'] == 2: icon = "ic_fire"
        elif meta['cat_id'] == 4: icon = "ic_water"
        else: icon = "ic_shelter"

        article = {
            "id": article_id,
            "categoryId": meta['cat_id'],
            "title": meta['title'],
            "content": cleaned_content,
            "tags": meta['tags'],
            "iconResName": icon,
            "lastReadTimestamp": 0,
            "level": "Уровень: Средний",
            "readTimeMin": 10,
            "lastUpdated": "10 Апреля 2026"
        }
        articles.append(article)
        article_id += 1

# Write categories
with open(os.path.join(DATA_DIR, "categories.json"), "w", encoding="utf-8") as f:
    json.dump(CATEGORIES, f, ensure_ascii=False, indent=2)

# Write articles
with open(os.path.join(DATA_DIR, "articles.json"), "w", encoding="utf-8") as f:
    json.dump(articles, f, ensure_ascii=False, indent=2)

print(f"Generated {len(CATEGORIES)} categories and {len(articles)} articles.")
