import os
import json
import re

WIKI_DIR = "wiki_tmp"
DATA_DIR = "app/src/main/assets/data"

categories = []
articles = []
cat_id = 1
art_id = 1

# Translate map for main categories
TRANSLATIONS = {
    "Home.md": "Главная",
    "Animals.md": "Опасные животные",
    "Apps.md": "Приложения",
    "BasicMechanicalSkills.md": "Базовые механические навыки",
    "BlackoutDriving.md": "Вождение в темноте",
    "Camouflage.md": "Маскировка",
    "CarRepair.md": "Ремонт автомобиля",
    "Cold.md": "Холодный климат",
    "Credits.md": "Благодарности",
    "DangerousArthropods.md": "Опасные членистоногие",
    "Desert.md": "Пустыня",
    "DirectionFinding.md": "Ориентирование",
    "FAQ.md": "Частые вопросы",
    "Fire.md": "Огонь",
    "FishAndMollusks.md": "Рыбы и моллюски",
    "Food.md": "Пища",
    "HostileAreas.md": "Враждебные территории",
    "Introduction.md": "Введение",
    "Kits.md": "Наборы",
    "ManMadeHazards.md": "Искусственные угрозы",
    "Medicine.md": "Медицина",
    "MultiTool.md": "Мультитул",
    "People.md": "Люди",
    "Planning.md": "Планирование",
    "Plants.md": "Растения",
    "Poisonous-Plants.md": "Ядовитые растения",
    "Power.md": "Энергия",
    "Psychology.md": "Психология",
    "RopesAndKnots.md": "Веревки и узлы",
    "Sea.md": "Море",
    "Self-defense.md": "Самооборона",
    "Shelter.md": "Укрытие",
    "Signaling.md": "Сигналы",
    "Tools.md": "Инструменты",
    "TranslatorNotes.md": "Заметки переводчика",
    "Tropical.md": "Тропики",
    "Water.md": "Вода",
    "WaterCrossing.md": "Переправа через воду",
    "Weapons.md": "Оружие"
}

def clean_markdown(content):
    lines = content.split('\n')
    cleaned_lines = []
    for line in lines:
        if line.startswith("Jump to bottom") or ("edited this page" in line and "revisions" in line) or line.startswith("Home · ligi"):
            continue
        img_match = re.search(r'!\[(.*?)\]\((.*?)\)', line)
        if img_match:
            alt_text = img_match.group(1).replace("'", "").replace('"', "")
            filename = os.path.basename(img_match.group(2))
            line = f"<image:{filename}:{alt_text}>"
        cleaned_lines.append(line)
    return '\n'.join(cleaned_lines).strip()

def split_into_articles(content, cat_id, cat_title):
    global art_id
    # Split by "### " or "## "
    # We will look for "### " or "## " that are at the beginning of a line
    
    sections = re.split(r'\n(#{2,3} )', content)
    
    # sections[0] is the intro before any headers
    intro = sections[0].strip()
    if intro and len(intro) > 50:
        articles.append({
            "id": art_id,
            "categoryId": cat_id,
            "title": f"Введение ({cat_title})",
            "content": intro,
            "tags": f"#{cat_title.replace(' ', '_').lower()}",
            "iconResName": "ic_fire",
            "lastReadTimestamp": 0,
            "level": "Уровень: Базовый",
            "readTimeMin": max(1, len(intro) // 500),
            "lastUpdated": "11 Апреля 2026",
            "isBookmarked": False
        })
        art_id += 1

    # the rest are pairs of delimiter and text
    for i in range(1, len(sections), 2):
        if i + 1 < len(sections):
            header_prefix = sections[i]
            body = sections[i+1]
            # First line of body is the title
            body_lines = body.split('\n')
            title = body_lines[0].strip()
            text = '\n'.join(body_lines[1:]).strip()
            
            if len(text) < 20: continue # ignore very short sections
            
            articles.append({
                "id": art_id,
                "categoryId": cat_id,
                "title": title,
                "content": text,
                "tags": f"#{cat_title.replace(' ', '_').lower()}",
                "iconResName": "ic_fire",
                "lastReadTimestamp": 0,
                "level": "Уровень: Средний",
                "readTimeMin": max(1, len(text) // 500),
                "lastUpdated": "11 Апреля 2026",
                "isBookmarked": False
            })
            art_id += 1


md_files = [f for f in os.listdir(WIKI_DIR) if f.endswith(".md") and not f.startswith("_")]
md_files.sort()

for filename in md_files:
    with open(os.path.join(WIKI_DIR, filename), 'r', encoding='utf-8') as f:
        content = f.read()
    
    content = clean_markdown(content)
    title = TRANSLATIONS.get(filename, filename.replace(".md", ""))
    
    # Assign an icon based on title
    icon = "ic_psychology"
    if "Вода" in title or "Море" in title: icon = "ic_water"
    elif "Огонь" in title or "Оружие" in title or "Инструменты" in title: icon = "ic_fire"
    elif "Укрытие" in title or "Приложения" in title or "Холод" in title: icon = "ic_shelter"

    categories.append({
        "id": cat_id,
        "title": title,
        "description": f"Раздел: {title}",
        "iconResName": icon
    })
    
    split_into_articles(content, cat_id, title)
    cat_id += 1

with open(os.path.join(DATA_DIR, "categories.json"), "w", encoding="utf-8") as f:
    json.dump(categories, f, ensure_ascii=False, indent=2)

with open(os.path.join(DATA_DIR, "articles.json"), "w", encoding="utf-8") as f:
    json.dump(articles, f, ensure_ascii=False, indent=2)

print(f"Generated {len(categories)} categories and {len(articles)} articles.")