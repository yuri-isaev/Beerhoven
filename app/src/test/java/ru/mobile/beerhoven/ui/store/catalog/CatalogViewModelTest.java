package ru.mobile.beerhoven.ui.store.catalog;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ru.mobile.beerhoven.interfaces.CrudRepository;
import ru.mobile.beerhoven.models.Item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CatalogViewModelTest {

   private CatalogViewModel mViewModel;
   private MutableLiveData<List<Item>> mMutableList;

   @Mock
   private CrudRepository mMockRepo;

   @Mock
   private List<String> mMockList;

   @Rule
   public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

   @Before
   public void setUp() {
      mMockRepo = mock(CrudRepository.class);
      mViewModel = new CatalogViewModel(mMockRepo);
      mMutableList = new MutableLiveData<>();
      getFakeItems();
      when(mMockRepo.getList()).thenReturn(mMutableList);
   }

   @Test
   public void test_returns_not_assertion_error() {
      String expected = "Not assertion error!";
      when(mMockList.get(0)).thenReturn(expected);
      String actual = mMockList.get(0);
      assertEquals(expected, actual);
      System.out.println(actual);
   }

   @Test
   public void viewModel_getCatalogList_returns_what_not_null() {
      // Method getList should return not null.
      MutableLiveData<List<Item>> result = mViewModel.getCatalogList();
      assertNotNull(result);
   }

   @Test
   public void viewModel_getCatalogList_returns_what_repository_getList() {
      // Test execution without Singleton pattern.
      // Method getList should return value quantity.
      MutableLiveData<List<Item>> result = mViewModel.getCatalogList();
      Assert.assertEquals(mMutableList.getValue().size(), result.getValue().size());
   }

   @Test
   public void viewModel_getCatalogList_calls_repository_getList() {
      // Test execution without Singleton pattern.
      // Method getList should calls method getList.
      mViewModel.getCatalogList();
      verify(mMockRepo, times(1)).getList();
   }

   private void getFakeItems() {
      List<Item> itemList = new ArrayList<>();
      itemList.add(new Item(UUID.randomUUID().toString(), "Test", 50.0, "1", "1"));
      mMutableList.setValue(itemList);
   }
}