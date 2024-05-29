package com.example.myapplication

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.collections.HashMap
import kotlin.collections.mapOf
import kotlin.collections.set


class MainActivity : ComponentActivity() {
    val db = FirebaseDatabase.getInstance()
    val referencia = db.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)
    }
    fun inactivarHorario(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_inactivarhorario, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = inactivarH))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }
    val inactivarH  = {dialog: DialogInterface, which: Int ->
        inactivarH()
    }
    fun inactivarH(){
        val buscar = findViewById<EditText>(R.id.buscando).text.toString()
        val bd = db.getReference("Horarios")
        bd.orderByChild("nombre").equalTo(buscar).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    for (horarioSnapshot in snapshot.children) {
                        val key: String = horarioSnapshot.key.toString()
                        val compare = horarioSnapshot.child("nombre").getValue(String::class.java)
                        if (compare == buscar){
                            bd.child(key).child("estado").setValue("inactivo").addOnSuccessListener {
                                Toast.makeText(applicationContext, "Horario inactivado correctamente", Toast.LENGTH_LONG).show()
                                setContentView(R.layout.inicio)
                            }.addOnFailureListener{
                                Toast.makeText(applicationContext, "El horario no pudo ser inactivado", Toast.LENGTH_LONG).show()
                                setContentView(R.layout.buscar_horario)
                            }
                    }
                }}

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })}

    fun inactivarGrupo(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_inactivarhorario, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = inactivarG))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }
    val inactivarG  = {dialog: DialogInterface, which: Int ->
        inactivarG()
    }
    fun inactivarG(){
        val buscar = findViewById<EditText>(R.id.buscar).text.toString()
        val bd = db.getReference("Grupos")
        bd.orderByChild("nombreGrupo").equalTo(buscar).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (horarioSnapshot in snapshot.children) {
                    val key: String = horarioSnapshot.key.toString()
                    val compare = horarioSnapshot.child("nombreGrupo").getValue(String::class.java)
                    if (compare == buscar){
                        bd.child(key).child("estado").setValue("inactivo").addOnSuccessListener {
                            Toast.makeText(applicationContext, "Grupo inactivado correctamente", Toast.LENGTH_LONG).show()
                            setContentView(R.layout.inicio)
                        }.addOnFailureListener{
                            Toast.makeText(applicationContext, "El grupo no pudo ser inactivado", Toast.LENGTH_LONG).show()
                            setContentView(R.layout.buscar_horario)
                        }
                    }
                }}

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })}

    fun viewEditarHorario(view: View){
        val buscando = findViewById<EditText>(R.id.buscando).text.toString()
        editarHorario(buscando)
    }
    fun editarHorario(buscando:String) {
        //val buscando = findViewById<EditText>(R.id.buscando).text.toString()
        setContentView(R.layout.editar_horario)

        val nombreHorarioEditText = findViewById<TextView>(R.id.nombreHE)
        val lunChbx = findViewById<CheckBox>(R.id.LunE)
        val marChbx = findViewById<CheckBox>(R.id.MarE)
        val mieChbx = findViewById<CheckBox>(R.id.MieE)
        val jueChbx = findViewById<CheckBox>(R.id.JueE)
        val vieChbx = findViewById<CheckBox>(R.id.VieE)
        val sabChbx = findViewById<CheckBox>(R.id.SabE)
        val domChbx = findViewById<CheckBox>(R.id.DomE)
        val horaIEditText = findViewById<EditText>(R.id.horaIHE)
        val horaFEditText = findViewById<EditText>(R.id.horaFHE)
        val bd = db.getReference("Horarios")

        // Verificar si el usuario existe en la base de datos
        bd.orderByChild("nombre").equalTo(buscando).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (horarioSnapshot in snapshot.children) {
                        val nombre = horarioSnapshot.child("nombre").getValue(String::class.java)
                        val dias = horarioSnapshot.child("dias").getValue(String::class.java)
                        val horaI = horarioSnapshot.child("horaI").getValue(String::class.java)
                        val horaF = horarioSnapshot.child("horaF").getValue(String::class.java)

                        // Mostrar los datos en los TextView correspondientes

                        nombreHorarioEditText.setText(nombre)
                        horaIEditText.setText(horaI)
                        horaFEditText.setText(horaF)
                        if (dias!!.contains("Lunes")){lunChbx.isChecked= true}
                        if (dias.contains("Martes")){marChbx.isChecked= true}
                        if (dias.contains("Miercoles")){mieChbx.isChecked= true}
                        if (dias.contains("Jueves")){jueChbx.isChecked= true}
                        if (dias.contains("Viernes")){vieChbx.isChecked= true}
                        if (dias.contains("Sabado")){sabChbx.isChecked= true}
                        if (dias.contains("Domingo")){domChbx.isChecked= true}
                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El horario no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })}
    fun updateHorario(view:View){

        // Actualizar los datos del usuario en la base de datos
        val bd = db.getReference("Horarios")
        val nombreHorario = findViewById<EditText>(R.id.nombreHE).text.toString()
        val dias = diasE()
        val horaI = findViewById<EditText>(R.id.horaIHE).text.toString()
        val horaF = findViewById<EditText>(R.id.horaFHE).text.toString()

        val horarioRef = referencia.child("Horarios")


        horarioRef.orderByChild("nombre").equalTo(nombreHorario).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (horarioSnapshot in snapshot.children) {
                            val key: String = horarioSnapshot.key.toString()
                            bd.child(key).child("nombre").setValue(nombreHorario)
                            bd.child(key).child("dias").setValue(dias)
                            bd.child(key).child("horaI").setValue(horaI)
                            bd.child(key).child("horaF").setValue(horaF).addOnSuccessListener {
                                Toast.makeText(applicationContext, "Horario editado correctamente", Toast.LENGTH_LONG).show()
                                setContentView(R.layout.inicio)
                            }.addOnFailureListener{
                                Toast.makeText(applicationContext, "El horario no pudo ser editado", Toast.LENGTH_LONG).show()
                                setContentView(R.layout.buscar_horario)
                            }

                    }}

                override fun onCancelled(error:  DatabaseError) {
                    Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
                }
            })

        }

    fun viewEditarGrupo(view: View){
        val buscando = findViewById<EditText>(R.id.buscar).text.toString()
        editarGrupo(buscando)
    }
    fun editarGrupo(buscando: String) {
        setContentView(R.layout.editar_grupo)

        val nombreGrupoEditText = findViewById<TextView>(R.id.nombreGE)
        val entrenadorTxt = findViewById<EditText>(R.id.entrenadorGE)
        val horarioTxt = findViewById<EditText>(R.id.HorarioGE)
        val lugarTxt = findViewById<EditText>(R.id.lugarGE)
        val integrantesTxt = findViewById<EditText>(R.id.integrantesE)
        val bd = db.getReference("Grupos")

        // Verificar si el usuario existe en la base de datos
        bd.orderByChild("nombreGrupo").equalTo(buscando).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (grupoSnapshot in snapshot.children) {
                        val entrenador = grupoSnapshot.child("entrenador").getValue(String::class.java)
                        val horario = grupoSnapshot.child("horario").getValue(String::class.java)
                        val lugar = grupoSnapshot.child("lugar").getValue(String::class.java)
                        val nombreGrupo = grupoSnapshot.child("nombreGrupo").getValue(String::class.java)
                        val integrantes = grupoSnapshot.child("integrantes").getValue(String::class.java)


                        entrenadorTxt.setText(entrenador)
                        horarioTxt.setText(horario)
                        lugarTxt.setText(lugar)
                        nombreGrupoEditText.setText(nombreGrupo)
                        integrantesTxt.setText(integrantes)

                    }
                } else {
                    Toast.makeText(applicationContext, "El grupo no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun updateGrupo(view:View){
        val buscando  = findViewById<TextView>(R.id.nombreGE).text.toString()
        val entrenador = findViewById<EditText>(R.id.entrenadorGE)
        val horario = findViewById<EditText>(R.id.HorarioGE)
        val lugar = findViewById<EditText>(R.id.lugarGE)
        val integrantes = findViewById<EditText>(R.id.integrantesE)

        val bd = db.getReference("Grupos")

        val grupoRef = referencia.child("Grupos")


        grupoRef.orderByChild("nombreGrupo").equalTo(buscando).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (horarioSnapshot in snapshot.children) {
                    val key: String = horarioSnapshot.key.toString()
                    bd.child(key).child("nombreGrupo").setValue(buscando)
                    bd.child(key).child("entrenador").setValue(entrenador)
                    bd.child(key).child("horario").setValue(horario)
                    bd.child(key).child("lugar").setValue(lugar)
                    bd.child(key).child("integrantes").setValue(integrantes).addOnSuccessListener {
                        Toast.makeText(applicationContext, "Grupo editado correctamente", Toast.LENGTH_LONG).show()
                        setContentView(R.layout.inicio)
                    }.addOnFailureListener{
                        Toast.makeText(applicationContext, "El grupo no pudo ser editado", Toast.LENGTH_LONG).show()
                        setContentView(R.layout.buscar_horario)
                    }

                }}

            override fun onCancelled(error:  DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun verHorario(view: View){
        val diasTxt = findViewById<TextView>(R.id.dias)
        val horasTxt = findViewById<TextView>(R.id.horas)
        val estadoTxt = findViewById<TextView>(R.id.estadoH)
        val buscarTxt = findViewById<EditText>(R.id.buscando)
        val buscar = buscarTxt.text.toString()

        buscarTxt.setText(buscar)

        val bd = db.getReference("Horarios")
        bd.orderByChild("nombre").equalTo(buscar).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (horarioSnapshot in snapshot.children) {
                        val dias = horarioSnapshot.child("dias").getValue(String::class.java)
                        val estado = horarioSnapshot.child("estado").getValue(String::class.java)
                        val horaI = horarioSnapshot.child("horaI").getValue(String::class.java)
                        val horaF = horarioSnapshot.child("horaF").getValue(String::class.java)
                        val horas = "Hora: " + horaI+ " a " + horaF
                        // Mostrar los datos en los TextView correspondientes

                        diasTxt.setText(dias)
                        horasTxt.setText(horas)
                        estadoTxt.setText(estado)

                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El horario no existe", Toast.LENGTH_SHORT)
                        .show()
                    diasTxt.setText("Horario no encontrado")
                    horasTxt.setText("Vuelva a intentar")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        }    )
    }
    fun buscarGrupo(view: View){
        val entrenadortxt = findViewById<TextView>(R.id.entrenadorGV)
        val horariotxt = findViewById<TextView>(R.id.horarioGV)
        val lugartxt = findViewById<TextView>(R.id.lugarGV)
        val estadoTxt = findViewById<TextView>(R.id.estadoG)
        val buscarTxt = findViewById<EditText>(R.id.buscar)
        val buscar = buscarTxt.text.toString()
        buscarTxt.setText(buscar)

        val bd = db.getReference("Grupos")
        bd.orderByChild("nombreGrupo").equalTo(buscar).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (grupoSnapshot in snapshot.children) {
                        val entrenador = grupoSnapshot.child("entrenador").getValue(String::class.java)
                        val horario = grupoSnapshot.child("horario").getValue(String::class.java)
                        val lugar = grupoSnapshot.child("lugar").getValue(String::class.java)
                        val estado = grupoSnapshot.child("estado").getValue(String::class.java)

                        // Mostrar los datos en los TextView correspondientes

                        entrenadortxt.setText(entrenador)
                        horariotxt.setText(horario)
                        lugartxt.setText(lugar)
                        estadoTxt.setText(estado)

                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El horario no existe", Toast.LENGTH_SHORT)
                        .show()
                    entrenadortxt.setText("Grupo no encontrado")
                    horariotxt.setText("Vuelva a intentar")
                    lugartxt.setText("")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        }    )
    }
    fun diasE():String{
        var dias = ""
        var lun = findViewById<CheckBox>(R.id.LunE)
        var mar = findViewById<CheckBox>(R.id.MarE)
        var mie = findViewById<CheckBox>(R.id.MieE)
        var jue = findViewById<CheckBox>(R.id.JueE)
        var vie = findViewById<CheckBox>(R.id.VieE)
        var sab = findViewById<CheckBox>(R.id.SabE)
        var dom = findViewById<CheckBox>(R.id.DomE)
        if (lun.isChecked){dias += "Lunes, "}
        if (mar.isChecked){dias += "Martes, "}
        if (mie.isChecked){dias += "Miercoles, "}
        if (jue.isChecked){dias += "Jueves, "}
        if (vie.isChecked){dias += "Viernes, "}
        if (sab.isChecked){dias += "Sabado, "}
        if (dom.isChecked){dias += "Domingo"}
        return dias
    }

    fun dias():String{
        var dias = ""
        var lun = findViewById<CheckBox>(R.id.Lun)
        var mar = findViewById<CheckBox>(R.id.Mar)
        var mie = findViewById<CheckBox>(R.id.Mie)
        var jue = findViewById<CheckBox>(R.id.Jue)
        var vie = findViewById<CheckBox>(R.id.Vie)
        var sab = findViewById<CheckBox>(R.id.Sab)
        var dom = findViewById<CheckBox>(R.id.Dom)
        if (lun.isChecked){dias += "Lunes, "}
        if (mar.isChecked){dias += "Martes, "}
        if (mie.isChecked){dias += "Miercoles, "}
        if (jue.isChecked){dias += "Jueves, "}
        if (vie.isChecked){dias += "Viernes, "}
        if (sab.isChecked){dias += "Sabado, "}
        if (dom.isChecked){dias += "Domingo"}
        return dias
    }

    fun crearHorario(view: View){
    val nombreHorario = findViewById<EditText>(R.id.nombreH).text.toString()
    val dias = dias()
    val horaI = findViewById<EditText>(R.id.horaI).text.toString()
    val horaF = findViewById<EditText>(R.id.horaF).text.toString()

    val horarioRef = referencia.child("Horarios").push()

    val mapa = mapOf(
        "nombre" to nombreHorario,
        "dias" to dias,
        "horaI" to horaI,
        "horaF" to horaF,
        "estado" to "activo"
    )

    horarioRef.setValue(mapa).addOnSuccessListener {
        Log.d("RegisterHorarioActivity", "Horario registrado exitosamente")
        Toast.makeText(applicationContext, "Horario creado correctamente", Toast.LENGTH_LONG).show()
        setContentView(R.layout.inicio)
    }
        .addOnFailureListener { e ->
            Log.e("RegisterHorarioActivity", "Error al registrar horario: $e")
            Toast.makeText(applicationContext, "Fallo al crear el horario, intente de nuevo",
                Toast.LENGTH_LONG).show()
            setContentView(R.layout.crear_horario)
        }

    }
    fun crearGrupo(view:View){
        val nombreGrupo = findViewById<EditText>(R.id.nombreG).text.toString()
        val entrenador = findViewById<EditText>(R.id.entrenador).text.toString()
        val horarioG = findViewById<EditText>(R.id.horarioG).text.toString()
        val lugar = findViewById<EditText>(R.id.lugarG).text.toString()
        val integrantes = findViewById<EditText>(R.id.spinner_integrantes).text.toString()
        val grupoRef = referencia.child("Grupos").push()
        val mapita = mapOf(
            "nombreGrupo" to nombreGrupo,
            "entrenador" to entrenador,
            "horarioG" to horarioG,
            "lugar" to lugar,
            "integrantes" to integrantes,
            "estado" to "activo"
        )

        grupoRef.setValue(mapita).addOnSuccessListener {
            Log.d("", "Grupo registrado exitosamente")
            Toast.makeText(applicationContext, "Grupo creado correctamente", Toast.LENGTH_LONG).show()
            setContentView(R.layout.inicio)
        }
            .addOnFailureListener { e ->
                Log.e("", "Error al registrar el grupo: $e")
                Toast.makeText(applicationContext, "Fallo al crear el grupo, intente de nuevo",
                    Toast.LENGTH_LONG).show()
                setContentView(R.layout.nuevo_grupo)
            }}
    fun Buscar_Perfil(view: View){
        setContentView(R.layout.buscar_usuario)
        val busquedaUsuarioEditText = findViewById<EditText>(R.id.busquedausuario)
        val buscarUsuarioButton = findViewById<Button>(R.id.buscando_usuarios)

        buscarUsuarioButton.setOnClickListener {
            val nombreUsuario = busquedaUsuarioEditText.text.toString()
            if (nombreUsuario == "") {
                Toast.makeText(applicationContext, "Porfavor ingrese un usuario para buscar", Toast.LENGTH_SHORT).show()
            } else {
                buscarUsuarioEnFirebase(nombreUsuario)
            }
        }
    }
    private fun buscarUsuarioEnFirebase(nombreUsuario: String) {
        val referenciaUsuarios = FirebaseDatabase.getInstance().getReference("Usuarios")
        referenciaUsuarios.orderByChild("Usuario").equalTo(nombreUsuario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // El usuario está en la base de datos, abrir el menú de usuarios
                        setContentView(R.layout.buscando_usuario)
                        val nombreEditText = findViewById<TextView>(R.id.nombreuserbusqueda)
                        nombreEditText.setText(nombreUsuario)
                        val verperfil = findViewById<LinearLayout>(R.id.miperfil)
                        val editarperfil = findViewById<LinearLayout>(R.id.EditarUser)
                        val inactivarperfil = findViewById<LinearLayout>(R.id.Inactivarusuario)
                        verperfil.setOnClickListener{
                            colocardatosusuario(nombreUsuario)
                        }
                        editarperfil.setOnClickListener{
                            editardatosusuario(nombreUsuario)
                        }
                        inactivarperfil.setOnClickListener{
                            Inactivarusuario(nombreUsuario)
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "El usuario ingresado no existe",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            applicationContext,
                            "Error al conectar con la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }) }
    // Dany

    private fun Inactivarusuario(nombreUsuario: String) {
        val database = FirebaseDatabase.getInstance()
        val referenciaUsuarios = database.getReference("Usuarios")
        referenciaUsuarios.child(nombreUsuario).child("Estado").setValue("Inactivo")
        Toast.makeText(applicationContext, "El usuario se ha inactivado", Toast.LENGTH_SHORT).show()
        setContentView(R.layout.usuario_menu)
    }

    private fun editardatosusuario(nombreUsuario: String) {
        setContentView(R.layout.editar_usuario)

        val nombreEditText = findViewById<EditText>(R.id.nombreuser)
        val usuarioEditText = findViewById<TextView>(R.id.usuario)
        val cedulaEditText = findViewById<EditText>(R.id.cedulauser)
        val grupoEditText = findViewById<EditText>(R.id.grupouser)
        val especialidadEditText = findViewById<EditText>(R.id.especialidaduser)
        val telefonoEditText = findViewById<EditText>(R.id.telefonouser)
        val guardarCambiosButton = findViewById<Button>(R.id.guardarcambios)

        // Obtener referencia a la base de datos
        val database = FirebaseDatabase.getInstance()
        val referenciaUsuarios = database.getReference("Usuarios")

        // Verificar si el usuario existe en la base de datos
        referenciaUsuarios.orderByChild("Usuario").equalTo(nombreUsuario).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // El usuario existe, obtener sus datos
                    for (usuarioSnapshot in snapshot.children) {
                        val nombre = usuarioSnapshot.child("Nombre").getValue(String::class.java)
                        val user = usuarioSnapshot.child("Usuario").getValue(String::class.java)
                        val cedula = usuarioSnapshot.child("Cedula").getValue(String::class.java)
                        val grupo = usuarioSnapshot.child("Grupo").getValue(String::class.java)
                        val especialidad = usuarioSnapshot.child("Especialidad").getValue(String::class.java)
                        val telefono = usuarioSnapshot.child("Telefono").getValue(String::class.java)

                        // Mostrar los datos en los TextView correspondientes

                        nombreEditText.setText(nombre)
                        usuarioEditText.text = user
                        cedulaEditText.setText(cedula)
                        grupoEditText.setText(grupo)
                        especialidadEditText.setText(especialidad)
                        telefonoEditText.setText(telefono)
                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })
        guardarCambiosButton.setOnClickListener {
            setContentView(R.layout.usuario_menu)
            val nuevoNombre = nombreEditText.text.toString()
            val nuevaCedula = cedulaEditText.text.toString()
            val nuevoGrupo = grupoEditText.text.toString()
            val nuevaEspecialidad = especialidadEditText.text.toString()
            val nuevoTelefono = telefonoEditText.text.toString()

            // Obtener el nombre de usuario del EditText
            val nombreUsuario = usuarioEditText.text.toString()

            // Actualizar los datos del usuario en la base de datos
            referenciaUsuarios.child(nombreUsuario).child("Nombre").setValue(nuevoNombre)
            referenciaUsuarios.child(nombreUsuario).child("Cedula").setValue(nuevaCedula)
            referenciaUsuarios.child(nombreUsuario).child("Grupo").setValue(nuevoGrupo)
            referenciaUsuarios.child(nombreUsuario).child("Especialidad").setValue(nuevaEspecialidad)
            referenciaUsuarios.child(nombreUsuario).child("Telefono").setValue(nuevoTelefono)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Éxito al actualizar los datos
                        Toast.makeText(applicationContext, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        // Error al actualizar los datos
                        Toast.makeText(applicationContext, "Error al actualizar los datos", Toast.LENGTH_SHORT).show()
                    }
                }
        }


    }


    private fun colocardatosusuario(nombreUsuario: String) {
        setContentView(R.layout.ver_perfil)

        val nombreTextView = findViewById<TextView>(R.id.nombreuser)
        val usuarioTextView = findViewById<TextView>(R.id.usuario)
        val cedulaTextView = findViewById<TextView>(R.id.cedulauser)
        val grupoTextView = findViewById<TextView>(R.id.grupouser)
        val especialidadTextView = findViewById<TextView>(R.id.especialidaduser)
        val telefonoTextView = findViewById<TextView>(R.id.telefonouser)
        val estadoTextView = findViewById<TextView>(R.id.estadouser) // TextView para mostrar el estado

        // Obtener referencia a la base de datos
        val database = FirebaseDatabase.getInstance()
        val referenciaUsuarios = database.getReference("Usuarios")

        // Verificar si el usuario existe en la base de datos
        referenciaUsuarios.orderByChild("Usuario").equalTo(nombreUsuario).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // El usuario existe, obtener sus datos
                    for (usuarioSnapshot in snapshot.children) {
                        val nombre = usuarioSnapshot.child("Nombre").getValue(String::class.java)
                        val user = usuarioSnapshot.child("Usuario").getValue(String::class.java)
                        val cedula = usuarioSnapshot.child("Cedula").getValue(String::class.java)
                        val grupo = usuarioSnapshot.child("Grupo").getValue(String::class.java)
                        val especialidad = usuarioSnapshot.child("Especialidad").getValue(String::class.java)
                        val telefono = usuarioSnapshot.child("Telefono").getValue(String::class.java)
                        val estado = usuarioSnapshot.child("Estado").getValue(String::class.java) // Obtener el estado

                        // Mostrar los datos en los TextView correspondientes
                        nombreTextView.text = nombre
                        usuarioTextView.text = user
                        cedulaTextView.text = cedula
                        grupoTextView.text = grupo
                        especialidadTextView.text = especialidad
                        telefonoTextView.text = telefono
                        estadoTextView.text = estado // Mostrar el estado en el TextView estadouser
                    }
                } else {
                    // El usuario no existe
                    Toast.makeText(applicationContext, "El usuario no existe", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })


    }

    // Dany

    fun Inicio_noti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_notificaciones)
    }

    fun Noti_vernoti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_notificaciones)
    }

    fun Vernoti_torneo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_torneo_mariposa)
    }
    fun Noti_modnoti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.modificar_notificaciones)
    }
    fun Modnoti_torneo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.editar_torneo_mariposa)
    }
    fun Noti_añnoti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.crear_notificacion)
    }
    fun Volvermenu_noti(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_notificaciones)
    }
    fun Volvermenu_inicio(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.inicio)
    }
    fun Asist_verasist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_asistencias)
    }
    fun Verasist_aprendiendover(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_ver)
    }
    fun Aprendiendover_aprendiendovermartes09(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_ver_martes09)
    }
    fun Asist_modasist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.modificar_asistencias)
    }
    fun Modasist_modaprendiendo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_modificar)
    }
    fun Modaprendiendo_modaprendiendomartes(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_modificar_martes09)
    }
    fun Asist_añasist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.crear_asistencia)
    }
    fun Añasist_añaprendiendo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.aprendiendo_nadar_crear)
    }
    fun Volvermenu_asist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_asistencia)
    }
    fun Inicio_asist(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_asistencia)
    }


    // Vale

    fun volverButtonClicked(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.menu_grupo)
    }
    fun boton_busqueda(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.buscar_grupo)
    }
    fun Añadir_Grupo(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.nuevo_grupo)
    }
    fun boton_modificar_grup(view: View){
        setContentView(R.layout.editar_grupo_activo)
        val spinnerEstado = findViewById<Spinner>(R.id.estado)
        val option_Estado = arrayOf("Activo", "Inactivo")
        spinnerEstado.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, option_Estado)
    }
    fun Buscar_Grupo(view: View){
        setContentView(R.layout.buscar_grupo)
    }
    fun Ver_Grupo(view: View){
        setContentView(R.layout.ver_grupo)
    }
    fun Cambiara_activo(view: View){
        setContentView(R.layout.editar_grupo)
    }

    // Saris

    fun horariom_horariov(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.buscar_horario)
    }

    fun horariom_horarioe(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.editar_horario)
    }

    fun Volvermenu_h(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.horario_menu)
    }

    fun Ver_semillero(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.semillero_ejemplo)
    }

    fun registroU(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.registro_usuario)
    }

    fun registroC(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.registro_clave)
    }

    fun volvermU(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.usuario_menu)
    }


    fun usuariom_usuariov(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.ver_perfil)
    }

    fun usuariom_usuarioe(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.registro_usuario)
        val buttonGuardar = findViewById<Button>(R.id.button_login)
        buttonGuardar.setOnClickListener {
            guardarDatosEnFirebase()
        }
    }

    private fun guardarDatosEnFirebase() {
        val database = FirebaseDatabase.getInstance()
        val referenciaUsuarios = database.getReference("Usuarios")
        val nombreUsuario = findViewById<EditText>(R.id.regis_user).text.toString()
        referenciaUsuarios.orderByChild("Usuario").equalTo(nombreUsuario).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // El usuario ya está registrado
                    Toast.makeText(applicationContext,"¡El usuario ya está registrado!",Toast.LENGTH_SHORT).show()
                } else {
                    val id = findViewById<EditText>(R.id.regis_user).text.toString()
                    val usuario = HashMap<String, Any>()
                    usuario["Usuario"] = findViewById<EditText>(R.id.regis_user).text.toString()
                    usuario["Nombre"] = findViewById<EditText>(R.id.registname).text.toString()
                    usuario["Cedula"] = findViewById<EditText>(R.id.registcedula).text.toString()
                    usuario["Grupo"] = findViewById<EditText>(R.id.resgistgroup).text.toString()
                    usuario["Especialidad"] = findViewById<EditText>(R.id.registespec).text.toString()
                    usuario["Telefono"] = findViewById<EditText>(R.id.registphone).text.toString()
                    usuario["Estado"] = findViewById<EditText>(R.id.estadouser).text.toString()
                    referenciaUsuarios.child(id).setValue(usuario)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Éxito
                                Toast.makeText(applicationContext,"¡El usuario se ha registrado correctamente!",Toast.LENGTH_SHORT).show()
                                setContentView(R.layout.inicio)
                            } else {
                                // Error
                                Toast.makeText(applicationContext,"¡Error desconocido! Intentlo de nuevo mas tarde",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de la consulta o proporcionar una implementación vacía
                Toast.makeText(applicationContext, "Error al consultar la base de datos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun horariom_horarioc(view: View) {
        // Código para manejar el clic del botón aquí
        setContentView(R.layout.crear_horario)
    }

    //

    fun EditarUsuario(view: View){
        setContentView(R.layout.editar_usuario)
    }

    fun Inicio_grupo(view: View){
        setContentView(R.layout.menu_grupo)
    }
    fun Asist_reporte(view: View){
        setContentView(R.layout.generar_repo)
    }
    fun Inicio_usuarios(view: View){
        setContentView(R.layout.usuario_menu)
    }
    fun Inicio_horarios(view: View){
        setContentView(R.layout.horario_menu)
    }
    fun Inicio_miperfil(view: View){
        setContentView(R.layout.ver_perfil)
    }
    fun Inicio_iniciarsesion(view: View){
        setContentView(R.layout.iniciar_sesion)
    }
    fun Iniciarsesion_inicio(view: View){
        setContentView(R.layout.inicio)
    }
    fun Presentacion_iniciarsesion(view: View){
        setContentView(R.layout.iniciar_sesion)

        val botonIniciarSesion = findViewById<Button>(R.id.irinicio)
        botonIniciarSesion.setOnClickListener {
            realizarInicioSesion()
        }
    }
    private fun realizarInicioSesion() {
        val nombreUsuario = findViewById<EditText>(R.id.user).text.toString()
        val contraseña = findViewById<EditText>(R.id.password).text.toString()

        if (nombreUsuario == "admintriton" && contraseña == "1234abc") {
            setContentView(R.layout.inicio)
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
    fun Registroclave_iniciosesion(view: View){
        setContentView(R.layout.iniciar_sesion)
    }
    fun Buscaruser_buscandousuario(view: View){
        setContentView(R.layout.buscando_usuario)
    }
    fun horarioc_volver(view: View){
        setContentView(R.layout.horario_menu)
    }

    //Pop ups

    val positiveButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
    }
    val negativeButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.no, Toast.LENGTH_SHORT).show()
    }
    val neutralButtonClick = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            "Maybe", Toast.LENGTH_SHORT).show()
    }

    val positiveButtonClickCrearAsist = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_asistencia)

    }

    fun Registrarasist(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_registrarasist, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickCrearAsist))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickModAsist = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_asistencia)
    }

    fun Modasist(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_modasist, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickModAsist))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickCrearNot = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_notificaciones)
    }

    fun Crearnoti(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_crearnoti, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickCrearNot))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickGuardarNot = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_notificaciones)
    }

    fun Guardarnoti(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_guardarnoti, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickGuardarNot))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickModGrup = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.menu_grupo)
    }

    fun Modificargrupo(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_modificargrupo, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickModGrup))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickCrearCuenta = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.iniciar_sesion)
    }

    fun Crearcuenta(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_crearcuenta, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickCrearCuenta))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }

    val positiveButtonClickEditarUser = { dialog: DialogInterface, which: Int ->
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        setContentView(R.layout.usuario_menu)
    }

    fun Editarusuario(view: View) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.popup_edituser, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(function = positiveButtonClickEditarUser))
        builder.setNegativeButton("Cancelar", DialogInterface.OnClickListener(function = negativeButtonClick))
        builder.show()

    }


}