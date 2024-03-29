package algonquin.cst2335.mobilegroupassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.application.databinding.SongDetailsAddBinding;
import com.android.application.databinding.SongListBinding;

public class MessageDetailsFragment extends Fragment {

    DeezerSong selected;

    public MessageDetailsFragment(DeezerSong m){
        selected = m;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        SongDetailsAddBinding binding = SongDetailsAddBinding.inflate(inflater);

        binding.titleView.setText(selected.getSong());
        binding.timeView.setText(selected.getTime());
        return binding.getRoot();

    }
}
