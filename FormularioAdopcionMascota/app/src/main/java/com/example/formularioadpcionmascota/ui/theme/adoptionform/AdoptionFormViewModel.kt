package com.example.formularioadpcionmascota.ui.theme.adoptionform

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.formularioadpcionmascota.model.AdoptionForm

class AdoptionFormViewModel : ViewModel() {

    var form by mutableStateOf(AdoptionForm())
        private set

    fun onNameChange(value: String)     { form = form.copy(fullName = value) }
    fun onEmailChange(value: String)    { form = form.copy(email = value) }
    fun onPhoneChange(value: String)    { form = form.copy(phone = value) }
    fun onAddressChange(value: String)  { form = form.copy(address = value) }
    fun onHousingChange(value: String)  { form = form.copy(housingType = value) }
    fun onLocationChange(value: String) { form = form.copy(location = value) }

    fun submitForm(): Boolean {
        return form.fullName.isNotBlank() &&
                form.email.isNotBlank() &&
                form.phone.isNotBlank() &&
                form.address.isNotBlank() &&
                form.housingType.isNotBlank() &&
                form.location.isNotBlank()
    }
}