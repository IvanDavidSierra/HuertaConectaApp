package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.ue.edu.huertaconectaapp.MainActivity;
import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.controller.AuthController;
import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.UserLogin;
import co.ue.edu.huertaconectaapp.model.db.DatabaseHelper;
import co.ue.edu.huertaconectaapp.model.db.dao.SesionDao;

public class LoginFragment extends Fragment {

    private EditText etCorreo, etContrasena;
    private Button btnIngresar;
    private AuthController authController;
    private SesionDao sesionDao;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public LoginFragment() {}

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity) getActivity()).showMenu(false);
        initObjects(view);
        return view;
    }

    private void initObjects(View view) {
        etCorreo = view.findViewById(R.id.etCorreo);
        etContrasena = view.findViewById(R.id.etContrasena);
        btnIngresar = view.findViewById(R.id.btnIngresar);
        authController = new AuthController();
        sesionDao = new SesionDao(DatabaseHelper.getInstance(requireContext()));
        btnIngresar.setOnClickListener(v -> login());

        TextView tvRegistrate = view.findViewById(R.id.tvRegistrate);
        String footer = getString(R.string.login_register_footer);
        String linkRegister = getString(R.string.link_register);
        SpannableString span = new SpannableString(footer);
        int start = footer.indexOf(linkRegister);
        if (start >= 0) {
            int end = start + linkRegister.length();
            int linkColor = ContextCompat.getColor(requireContext(), R.color.primaryColor);
            span.setSpan(new ForegroundColorSpan(linkColor), start, end, 0);
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).loadFragment(new RegisterFragment());
                    }
                }
            }, start, end, 0);
        }
        tvRegistrate.setText(span);
        tvRegistrate.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void login() {
        String correo = etCorreo.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();

        UserLogin userLogin = new UserLogin(correo, contrasena);
        authController.login(userLogin, new AuthController.AuthListener() {
            @Override
            public void onSuccess(AuthResult result) {
                String fechaLogin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                int idUsuario = (result.getUser() != null) ? result.getUser().getIdUsuario() : 0;
                sesionDao.guardarSesion(result.getToken(), correo, fechaLogin, idUsuario);
                requireActivity().runOnUiThread(() -> {
                    ((MainActivity) getActivity()).showMenu(true);
                    ((MainActivity) getActivity()).loadFragment(new HomeFragment());
                });
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
