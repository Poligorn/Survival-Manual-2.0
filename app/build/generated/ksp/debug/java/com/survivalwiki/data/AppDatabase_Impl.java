package com.survivalwiki.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.FtsTableInfo;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.survivalwiki.data.dao.ArticleDao;
import com.survivalwiki.data.dao.ArticleDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile ArticleDao _articleDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(8) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `categories` (`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT, `iconResName` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `articles` (`id` INTEGER NOT NULL, `categoryId` INTEGER NOT NULL, `title` TEXT NOT NULL, `content` TEXT NOT NULL, `tags` TEXT NOT NULL, `iconResName` TEXT NOT NULL, `lastReadTimestamp` INTEGER NOT NULL, `level` TEXT, `readTimeMin` INTEGER NOT NULL, `lastUpdated` TEXT, `isBookmarked` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `articles_fts` USING FTS4(`title` TEXT NOT NULL, `content` TEXT NOT NULL, `tags` TEXT NOT NULL, content=`articles`)");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_BEFORE_UPDATE BEFORE UPDATE ON `articles` BEGIN DELETE FROM `articles_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_BEFORE_DELETE BEFORE DELETE ON `articles` BEGIN DELETE FROM `articles_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_AFTER_UPDATE AFTER UPDATE ON `articles` BEGIN INSERT INTO `articles_fts`(`docid`, `title`, `content`, `tags`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`content`, NEW.`tags`); END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_AFTER_INSERT AFTER INSERT ON `articles` BEGIN INSERT INTO `articles_fts`(`docid`, `title`, `content`, `tags`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`content`, NEW.`tags`); END");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a6231d084cbb025e55a1f6c609115a08')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `categories`");
        db.execSQL("DROP TABLE IF EXISTS `articles`");
        db.execSQL("DROP TABLE IF EXISTS `articles_fts`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_BEFORE_UPDATE BEFORE UPDATE ON `articles` BEGIN DELETE FROM `articles_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_BEFORE_DELETE BEFORE DELETE ON `articles` BEGIN DELETE FROM `articles_fts` WHERE `docid`=OLD.`rowid`; END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_AFTER_UPDATE AFTER UPDATE ON `articles` BEGIN INSERT INTO `articles_fts`(`docid`, `title`, `content`, `tags`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`content`, NEW.`tags`); END");
        db.execSQL("CREATE TRIGGER IF NOT EXISTS room_fts_content_sync_articles_fts_AFTER_INSERT AFTER INSERT ON `articles` BEGIN INSERT INTO `articles_fts`(`docid`, `title`, `content`, `tags`) VALUES (NEW.`rowid`, NEW.`title`, NEW.`content`, NEW.`tags`); END");
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsCategories = new HashMap<String, TableInfo.Column>(4);
        _columnsCategories.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategories.put("iconResName", new TableInfo.Column("iconResName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategories = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategories = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategories = new TableInfo("categories", _columnsCategories, _foreignKeysCategories, _indicesCategories);
        final TableInfo _existingCategories = TableInfo.read(db, "categories");
        if (!_infoCategories.equals(_existingCategories)) {
          return new RoomOpenHelper.ValidationResult(false, "categories(com.survivalwiki.data.entity.Category).\n"
                  + " Expected:\n" + _infoCategories + "\n"
                  + " Found:\n" + _existingCategories);
        }
        final HashMap<String, TableInfo.Column> _columnsArticles = new HashMap<String, TableInfo.Column>(11);
        _columnsArticles.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("tags", new TableInfo.Column("tags", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("iconResName", new TableInfo.Column("iconResName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("lastReadTimestamp", new TableInfo.Column("lastReadTimestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("level", new TableInfo.Column("level", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("readTimeMin", new TableInfo.Column("readTimeMin", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("lastUpdated", new TableInfo.Column("lastUpdated", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsArticles.put("isBookmarked", new TableInfo.Column("isBookmarked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysArticles = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesArticles = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoArticles = new TableInfo("articles", _columnsArticles, _foreignKeysArticles, _indicesArticles);
        final TableInfo _existingArticles = TableInfo.read(db, "articles");
        if (!_infoArticles.equals(_existingArticles)) {
          return new RoomOpenHelper.ValidationResult(false, "articles(com.survivalwiki.data.entity.Article).\n"
                  + " Expected:\n" + _infoArticles + "\n"
                  + " Found:\n" + _existingArticles);
        }
        final HashSet<String> _columnsArticlesFts = new HashSet<String>(3);
        _columnsArticlesFts.add("title");
        _columnsArticlesFts.add("content");
        _columnsArticlesFts.add("tags");
        final FtsTableInfo _infoArticlesFts = new FtsTableInfo("articles_fts", _columnsArticlesFts, "CREATE VIRTUAL TABLE IF NOT EXISTS `articles_fts` USING FTS4(`title` TEXT NOT NULL, `content` TEXT NOT NULL, `tags` TEXT NOT NULL, content=`articles`)");
        final FtsTableInfo _existingArticlesFts = FtsTableInfo.read(db, "articles_fts");
        if (!_infoArticlesFts.equals(_existingArticlesFts)) {
          return new RoomOpenHelper.ValidationResult(false, "articles_fts(com.survivalwiki.data.entity.ArticleFts).\n"
                  + " Expected:\n" + _infoArticlesFts + "\n"
                  + " Found:\n" + _existingArticlesFts);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "a6231d084cbb025e55a1f6c609115a08", "2d69e960f011e2202f1e2ae1e5aae8f2");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(1);
    _shadowTablesMap.put("articles_fts", "articles");
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "categories","articles","articles_fts");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `categories`");
      _db.execSQL("DELETE FROM `articles`");
      _db.execSQL("DELETE FROM `articles_fts`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(ArticleDao.class, ArticleDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public ArticleDao articleDao() {
    if (_articleDao != null) {
      return _articleDao;
    } else {
      synchronized(this) {
        if(_articleDao == null) {
          _articleDao = new ArticleDao_Impl(this);
        }
        return _articleDao;
      }
    }
  }
}
