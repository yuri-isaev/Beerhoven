package ru.mobile.beerhoven.interfaces;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.models.Item;

public interface CrudRepository<T>{
   public MutableLiveData<List<Item>> getList();
}
