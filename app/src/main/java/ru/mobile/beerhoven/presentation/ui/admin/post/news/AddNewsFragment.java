package ru.mobile.beerhoven.presentation.ui.admin.post.news;

import static java.util.Objects.requireNonNull;
import static ru.mobile.beerhoven.utils.Validation.isValidTextField;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.databinding.FragmentAddNewsBinding;
import ru.mobile.beerhoven.domain.model.News;
import ru.mobile.beerhoven.presentation.activity.MainActivity;
import ru.mobile.beerhoven.presentation.ui.admin.post.PostFragment;
import ru.mobile.beerhoven.utils.Toasty;

public class AddNewsFragment extends PostFragment {
   private Button mAddDatabaseButton;
   private ImageView mNewsImage;
   private ImageView mAddImageSelector;
   private TextInputLayout mTitleInput;
   private TextInputLayout mDescriptionInput;

   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      FragmentAddNewsBinding binding = FragmentAddNewsBinding.inflate(inflater, container, false);
      mAddDatabaseButton = binding.btnAddDatabase;
      mAddImageSelector = binding.ivSelectorAddImage;
      mTitleInput = binding.ilNewsTitle;
      mDescriptionInput = binding.ilNewsDescription;
      mNewsImage = binding.ivNewsImage;
      return binding.getRoot();
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
      super.onViewCreated(view, savedInstanceState);
      AddNewsViewModel viewModel = new AddNewsViewModel();
      Date currentDate = new Date();

      mAddImageSelector.setOnClickListener(v -> super
          .onShowImagePickDialog()
          .observe(getViewLifecycleOwner(), bitmap -> mNewsImage.setImageBitmap(bitmap)));

      mAddDatabaseButton.setOnClickListener(v -> {
         if (!isValidTextField(mTitleInput) | !isValidTextField(mDescriptionInput)) {
            Toasty.error(requireActivity(), R.string.invalid_form);
         }
         // else if (super.mUriImage == null) {
         // Toasty.error(requireActivity(), R.string.add_image);}
         else {
            String description = requireNonNull(mDescriptionInput.getEditText()).getText().toString();
            String time = String.valueOf(currentDate);
            String title = requireNonNull(mTitleInput.getEditText()).getText().toString();

            News news = new News();
            news.setDescription(description);
            news.setId("null");
            news.setTime(time);
            news.setTitle(title);
            news.setImage(super.mUriImage != null ? super.mUriImage.toString() : "null");

            ((MainActivity) requireActivity()).onIncreaseNewsCounter();
            viewModel.onAddNewsToRepository(requireActivity(), news);
         }
      });
   }
}
