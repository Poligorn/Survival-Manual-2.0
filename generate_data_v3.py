import re
import urllib.request
import ssl
import os
import json

ssl._create_default_https_context = ssl._create_unverified_context

WIKI_DIR = "wiki_tmp"
DATA_DIR = "app/src/main/assets/data"

structure = [
    ("Введение", "Введение", "Introduction.md", "ic_local_fire_department"),
    ("Психология", "Психология выживания", "Psychology.md", "ic_psychology"),
    ("Планирование", "Планирование выживания", "Planning.md", "ic_map"),
    ("Наборы", "Наборы для выживания", "Kits.md", "ic_build"),
    ("Приложения", "Полезные приложения для выживания", "Apps.md", "ic_phone_android"),
    ("Базовая медицина", "Основы выживания в медицине", "Medicine.md", "ic_medical_services"),
    ("Укрытия", "Постройка укрытий", "Shelter.md", "ic_house"),
    ("Добыча воды", "Методы добычи воды", "Water.md", "ic_water_drop"),
    ("Разведение огня", "Техники разведения огня", "Fire.md", "ic_local_fire_department"),
    ("Добыча пищи", "Методы добычи пищи", "Food.md", "ic_restaurant"),
    ("Использование растений", "Полезные растения для выживания", "Plants.md", "ic_forest"),
    ("Опасные животные", "Взаимодействие с опасными животными", "Animals.md", "ic_pets"),
    ("Инструменты и оборудование", "Самодельные инструменты и оружие", "Tools.md", "ic_build"),
    ("Ремонт автомобиля", "Основы ремонта автомобиля", "CarRepair.md", "ic_directions_car"),
    ("Базовые механические навыки", "Механические навыки для выживания", "BasicMechanicalSkills.md", "ic_build"),
    ("Вождение без света", "Техники вождения в темноте", "BlackoutDriving.md", "ic_directions_car"),
    ("Оружие", "Использование оружия в условиях выживания", "Weapons.md", "ic_warning"),
    ("Пустыня", "Выживание в пустыне", "Desert.md", "ic_wb_sunny"),
    ("Тропики", "Выживание в тропиках", "Tropical.md", "ic_park"),
    ("Холодная погода", "Выживание в холодном климате", "Cold.md", "ic_ac_unit"),
    ("Море", "Выживание на море", "Sea.md", "ic_sailing"),
    ("Переправа через воду", "Самодельные способы переправы через воду", "WaterCrossing.md", "ic_sailing"),
    ("Ориентирование", "Методы ориентирования на местности", "DirectionFinding.md", "ic_explore"),
    ("Сигнальные техники", "Способы подачи сигналов спасения", "Signaling.md", "ic_campaign"),
    ("Выживание в враждебных районах", "Тактики выживания в опасных зонах", "HostileAreas.md", "ic_warning"),
    ("Камуфляж", "Методы маскировки и камуфляжа", "Camouflage.md", "ic_visibility_off"),
    ("Контакт с местными жителями", "Взаимодействие с местным населением", "People.md", "ic_people"),
    ("Опасные членистоногие", "Опасные насекомые и паукообразные", "DangerousArthropods.md", "ic_bug_report"),
    ("Рыбы и моллюски", "Добыча и приготовление рыбы и моллюсков", "FishAndMollusks.md", "ic_set_meal"),
    ("Электроэнергия", "Генерация и использование электроэнергии в условиях выживания", "Power.md", "ic_bolt"),
    ("Узлы", "Основные узлы и их применение", "RopesAndKnots.md", "ic_gesture"),
    ("Черный список", "Вещи, которых следует избегать в условиях выживания", "ManMadeHazards.md", "ic_cancel"),
]

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
            continue
            
        line = re.sub(r'\*\*(.*?)\*\*', r'\1', line)
        line = re.sub(r'__(.*?)__', r'\1', line)
        line = re.sub(r'\*(.*?)\*', r'\1', line)
        line = re.sub(r'_(.*?)_', r'\1', line)
        
        line = re.sub(r'\[(.*?)\]\(.*?\)', r'\1', line)
        
        if line.startswith('> '):
            line = line[2:]
            
        stripped = line.strip()
        if stripped.startswith('* ') or stripped.startswith('- '):
            line = line.replace('* ', '• ', 1).replace('- ', '• ', 1)
            
        if not line.startswith('<image:') and not line.startswith('##') and not line.startswith('#'):
            line = re.sub(r'<[^>]+>', '', line)
            
        line = re.sub(r'`(.*?)`', r'\1', line)
        
        cleaned_lines.append(line)
    return '\n'.join(cleaned_lines).strip()

categories = []
articles = []

for idx, (cat_title, art_title, filename, icon) in enumerate(structure):
    cat_id = idx + 1
    art_id = idx + 1
    
    categories.append({
        "id": cat_id,
        "title": cat_title,
        "description": "",
        "iconResName": icon
    })
    
    if filename == "Introduction.md":
        try:
            url = "https://raw.githubusercontent.com/wiki/ligi/SurvivalManual/Introduction.md"
            req = urllib.request.Request(url)
            with urllib.request.urlopen(req) as response:
                content = response.read().decode('utf-8')
        except Exception as e:
            print(f"Failed to fetch Introduction.md from web: {e}")
            with open(os.path.join(WIKI_DIR, filename), 'r', encoding='utf-8') as f:
                content = f.read()
    else:
        file_path = os.path.join(WIKI_DIR, filename)
        if os.path.exists(file_path):
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
        else:
            content = "Контент в разработке..."
        
    content = clean_markdown(content)
    
    articles.append({
        "id": art_id,
        "categoryId": cat_id,
        "title": art_title,
        "content": content,
        "tags": f"#{cat_title.replace(' ', '_').lower()}",
        "iconResName": icon,
        "lastReadTimestamp": 0,
        "level": "Уровень: Базовый",
        "readTimeMin": max(1, len(content) // 500),
        "lastUpdated": "11 Апреля 2026",
        "isBookmarked": False
    })

with open(os.path.join(DATA_DIR, "categories.json"), "w", encoding="utf-8") as f:
    json.dump(categories, f, ensure_ascii=False, indent=2)

with open(os.path.join(DATA_DIR, "articles.json"), "w", encoding="utf-8") as f:
    json.dump(articles, f, ensure_ascii=False, indent=2)

print(f"Generated {len(categories)} categories and {len(articles)} articles.")