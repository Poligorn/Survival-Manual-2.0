import json
import os

DATA_DIR = "app/src/main/assets/data"
ARTICLES_FILE = os.path.join(DATA_DIR, "articles.json")

# 1. Read the translated "Introduction" content
with open(ARTICLES_FILE, 'r', encoding='utf-8') as f:
    articles = json.load(f)
    
intro_content_ru = None
for art in articles:
    if art['id'] == 1:
        intro_content_ru = art['content']
        break

if not intro_content_ru:
    print("Could not find Introduction article")
    exit(1)

# 2. Run generate_data_v3.py to recreate original articles
print("Running generate_data_v3.py to restore English content...")
os.system("python3 generate_data_v3.py")
os.system("python3 update_levels.py")

# 3. Read the newly generated articles
with open(ARTICLES_FILE, 'r', encoding='utf-8') as f:
    new_articles = json.load(f)

# 4. Inject the Russian introduction back
for art in new_articles:
    if art['id'] == 1:
        art['content'] = intro_content_ru
        break

# 5. Save the updated articles
with open(ARTICLES_FILE, 'w', encoding='utf-8') as f:
    json.dump(new_articles, f, ensure_ascii=False, indent=2)

print("Successfully restored original English articles and kept Russian Introduction.")
