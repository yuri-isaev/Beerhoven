package ru.mobile.beerhoven.domain.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ru.mobile.beerhoven.domain.model.Product;

public interface CrudRepository<T> {
   MutableLiveData<List<Product>> readList();
   MutableLiveData<String> createItem();
   MutableLiveData<String> deleteItem();
}
