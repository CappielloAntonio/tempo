package com.cappielloantonio.play.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cappielloantonio.play.R;
import com.cappielloantonio.play.adapter.ServerAdapter;
import com.cappielloantonio.play.databinding.FragmentLoginBinding;
import com.cappielloantonio.play.ui.activity.MainActivity;
import com.cappielloantonio.play.ui.fragment.dialog.ServerSignupDialog;
import com.cappielloantonio.play.viewmodel.LoginViewModel;

import java.util.Collections;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    private FragmentLoginBinding bind;
    private MainActivity activity;
    private LoginViewModel loginViewModel;

    private ServerAdapter serverAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.login_page_menu, menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        bind = FragmentLoginBinding.inflate(inflater, container, false);
        View view = bind.getRoot();

        initAppBar();
        initServerListView();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

    private void initAppBar() {
        activity.setSupportActionBar(bind.toolbar);

        bind.appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((bind.serverInfoSector.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(bind.toolbar))) {
                bind.toolbar.setTitle("Subsonic servers");
            } else {
                bind.toolbar.setTitle("");
            }
        });
    }

    private void initServerListView() {
        bind.serverListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        bind.serverListRecyclerView.setHasFixedSize(true);

        serverAdapter = new ServerAdapter(activity, requireContext());
        bind.serverListRecyclerView.setAdapter(serverAdapter);
        loginViewModel.getServerList().observe(requireActivity(), servers -> {
            if(servers.size() > 0) {
                if (bind != null) bind.noServerAddedTextView.setVisibility(View.GONE);
                if (bind != null) bind.serverListRecyclerView.setVisibility(View.VISIBLE);
                serverAdapter.setItems(servers);
            }
            else {
                if (bind != null) bind.noServerAddedTextView.setVisibility(View.VISIBLE);
                if (bind != null) bind.serverListRecyclerView.setVisibility(View.GONE);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            int originalPosition = -1;
            int fromPosition = -1;
            int toPosition = -1;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                if (originalPosition == -1)
                    originalPosition = viewHolder.getBindingAdapterPosition();

                fromPosition = viewHolder.getBindingAdapterPosition();
                toPosition = target.getBindingAdapterPosition();

                Collections.swap(serverAdapter.getItems(), fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                return false;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);

                loginViewModel.orderServer(serverAdapter.getItems());

                originalPosition = -1;
                fromPosition = -1;
                toPosition = -1;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        loginViewModel.deleteServer(serverAdapter.getItem(viewHolder.getBindingAdapterPosition()));
                        viewHolder.itemView.setBackgroundColor(Color.RED);
                        break;
                    case ItemTouchHelper.RIGHT:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("server_object", serverAdapter.getItem(viewHolder.getBindingAdapterPosition()));

                        ServerSignupDialog dialog = new ServerSignupDialog();
                        dialog.setArguments(bundle);
                        dialog.show(activity.getSupportFragmentManager(), null);

                        bind.serverListRecyclerView.getAdapter().notifyDataSetChanged();
                        break;
                }
            }
        }
        ).attachToRecyclerView(bind.serverListRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                ServerSignupDialog dialog = new ServerSignupDialog();
                dialog.show(activity.getSupportFragmentManager(), null);
                return true;
            default:
                break;
        }

        return false;
    }
}
