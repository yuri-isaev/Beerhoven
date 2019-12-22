package ru.mobile.beerhoven.data.storage;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.models.Item;

public interface CrudRepository<T> {
   MutableLiveData<List<Item>> readList();
   MutableLiveData<String> createItem();
   MutableLiveData<String> deleteItem();
}
