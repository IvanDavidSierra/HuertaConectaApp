    package co.ue.edu.huertaconectaapp.views;

    import android.graphics.Color;
    import android.os.Bundle;

    import androidx.annotation.NonNull;
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

    import org.w3c.dom.Text;

    import co.ue.edu.huertaconectaapp.MainActivity;
    import co.ue.edu.huertaconectaapp.R;
    import co.ue.edu.huertaconectaapp.controller.AuthController;
    import co.ue.edu.huertaconectaapp.model.AuthResult;
    import co.ue.edu.huertaconectaapp.model.UserLogin;

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link LoginFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public class LoginFragment extends Fragment {
        private EditText etCorreo, etContrasena;
        private Button btnIngresar;
        private AuthController authController;


        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        public LoginFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment loginFragment.
         */
        // TODO: Rename and change types and number of parameters
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

        private void initObjects(View view){
            etCorreo = view.findViewById(R.id.etCorreo);
            etContrasena = view.findViewById(R.id.etContrasena);
            btnIngresar = view.findViewById(R.id.btnIngresar);
            authController = new AuthController();
            btnIngresar.setOnClickListener(v-> login());
            TextView tvRegistrate = view.findViewById(R.id.tvRegistrate);
            SpannableString span = new SpannableString("¿No tienes cuenta? Registrate");
            span.setSpan(new ForegroundColorSpan(Color.parseColor("#6200EE")),19,29,0);
            span.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    ((MainActivity) getActivity()).loadFragment(new RegisterFragment());
                }
            },19,29,0);
            tvRegistrate.setText(span);
            tvRegistrate.setMovementMethod(LinkMovementMethod.getInstance());

        }

        private void login(){
            String correo = etCorreo.getText().toString();
            String contrasena = etContrasena.getText().toString();

            UserLogin userLogin = new UserLogin(correo,contrasena);
            authController.login(userLogin, new AuthController.AuthListener() {
                @Override
                public void onSuccess(AuthResult result) {
                    requireActivity().runOnUiThread(()->{
                        ((MainActivity) getActivity()).showMenu(true);
                        ((MainActivity) getActivity()).loadFragment(new HomeFragment());
                    });
                }

                @Override
                public void onError(String message) {
                    requireActivity().runOnUiThread(()->{
                        Toast.makeText(getContext(),"Error: " + message, Toast.LENGTH_SHORT).show();
                    });
                }
            });

        }


    }