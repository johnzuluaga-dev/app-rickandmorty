package com.danidev.apprickmorty.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    // Obtener usuario autenticado actual
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Registrar usuario con email y contraseña
    fun registrar(email: String, pass: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    onSuccess(uid)
                } else {
                    onError(task.exception?.message ?: "Error al registrar usuario")
                }
            }
    }

    // Iniciar sesión
    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSuccess()
                else onError(task.exception?.message ?: "Error al iniciar sesión")
            }
    }

    // Cerrar sesión
    fun logout() {
        auth.signOut()
    }

    // Guardar datos del usuario y la URL de la foto en Firestore
    fun guardarPerfil(nombre: String, email: String, photoUrl: String, onSuccess: () -> Unit, onError: (String) -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return
        val userMap = hashMapOf(
            "uid" to uid,
            "nombre" to nombre,
            "email" to email,
            "photoUrl" to photoUrl
        )
        db.collection("users").document(uid).set(userMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error al guardar en Firestore") }
    }

    // Obtener datos del perfil desde Firestore
    fun obtenerPerfil(onSuccess: (Map<String, Any>?) -> Unit, onError: (String) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    onSuccess(document.data)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error al obtener perfil")
            }
    }
}