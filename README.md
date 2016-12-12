# DuplicatelessORMLite
Duplicateless creation of new records for ORM Lite

### Description

By default ORMLite doesn't supports *create-if-not-exists* logic. Of course `BaseDaoImpl` class has `createIfNotExists` and 
`createOrUpdate` methods, but they both just make checks on id fields. 
That is if passed `DTO` has default id value (0 or null) new record will be created.

This tiny library extends `BaseDaoImpl` and adds `insertIfNotExists` method, which creates records in database only if they 
aren't present yet. 
Basically it performs check of all values except id.

### Instalation

Add this dependency to the `build.gradle`:

```
compile 'com.github.mikeherasimov:duplicatelessormlite:0.1'
```

### Configuration steps

- set `DuplicatelessDaoImpl.class` as `daoClass` for your `DTOs`, for example:
```
@DatabaseTable(tableName = Food.TABLE_NAME, daoClass = DuplicatelessDaoImpl.class)
public class Food {
...
}
```
- don't use primitive types for fields, except id field (it can cause exception, however I'm not sure)

Also there is properly configured example in `sample` directory, so you may want to check it.
