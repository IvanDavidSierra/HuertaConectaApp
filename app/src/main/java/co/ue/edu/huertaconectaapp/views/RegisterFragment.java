package co.ue.edu.huertaconectaapp.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import co.ue.edu.huertaconectaapp.MainActivity;
import co.ue.edu.huertaconectaapp.R;
import co.ue.edu.huertaconectaapp.controller.AuthController;
import co.ue.edu.huertaconectaapp.model.AuthResult;
import co.ue.edu.huertaconectaapp.model.UserRegister;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    private EditText etNombres, etApellidos, etCorreo, etContrasena, etContrasena2;
    private Button btnIngresar;
    private AuthController authController;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment registerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initObjects(view);
        return view;

    }
    private void initObjects(View view){
        etNombres = view.findViewById(R.id.etNombres);
        etApellidos = view.findViewById(R.id.etApellidos);
        etCorreo = view.findViewById(R.id.etRegisterCorreo);
        etContrasena = view.findViewById(R.id.etRegisterContrasena);
        etContrasena2 = view.findViewById(R.id.etRegisterContrasena2);
        btnIngresar = view.findViewById(R.id.btnIngresar);
        authController = new AuthController();
        btnIngresar.setOnClickListener(v->register());
    }
    private void register() {
        String nombre = etNombres.getText().toString();
        String apellido = etApellidos.getText().toString();
        String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();

        UserRegister user = new UserRegister(nombre, apellido, correo, contrasena, 1);
        authController.register(user, new AuthController.AuthListener() {
            @Override
            public void onSuccess(AuthResult result) {
                requireActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).loadFragment(new LoginFragment());
                });
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(()->{
                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                });
            }


        });


    }

}