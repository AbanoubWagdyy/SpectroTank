package com.spectrotank.ui.form

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.spectrotank.DataLayer.db.Item
import com.spectrotank.ui.imeValidation.FormPresenter
import kotlinx.android.synthetic.main.activity_form.*
import im.delight.android.location.SimpleLocation
import android.widget.Toast
import com.github.dhaval2404.imagepicker.ImagePicker
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.util.Base64
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import com.com.spectrotankapplication.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import android.location.LocationManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import com.com.spectrotankapplication.ErrorHandler

class FormActivity : AppCompatActivity(), FormContract.View {

    override fun handleSavedImeNumber(imeNumber: String?) {
        this.imeNumber = imeNumber
    }

    override fun showInvalidItemType() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Invalid Type ,please choose valid item type !.",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun showCompleteAllFieldsMessage() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Please complete all fields",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun finalizeScreen() {
        finish()
    }

    override fun showSuccessUpdate() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Device Attributes updated successfully",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showSuccessInsert() {
        Snackbar.make(
            findViewById(android.R.id.content),
            "Device Attributes saved successfully",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun handleSavedItem(item: Item) {

        disableCopyPasteToAllAttributes()

        tvIMEILabel.text = "IMEI : {$item.IMEI}"
        etWidth.setText(item.width.toString())
        etHeight.setText(item.height.toString())
        etLength.setText(item.length.toString())
        etDiameter.setText(item.diameter.toString())
        etName.setText(item.name.toString())

        if (item.imgUrlStr != null && !item.imgUrlStr.equals("")) {
            etPhoto.setText(item.imgUrlStr)
            ivPhoto.visibility = View.VISIBLE
            getBitmap(item.imgUrlStr, ivPhoto)
        }

        if (item.imgUrl != null)
            encodedImage = item.imgUrl!!

        var entries = applicationContext.resources.getStringArray(R.array.type_real)
        val index = entries.indexOf(item.type)

        spinnerType.selectedIndex = index

        if (index != 0)
            ivShape.visibility = View.VISIBLE
    }

    private fun disableCopyPasteToAllAttributes() {
        etWidth.setCustomSelectionActionModeCallback(callback)
        etHeight.setCustomSelectionActionModeCallback(callback)
        etLength.setCustomSelectionActionModeCallback(callback)
        etDiameter.setCustomSelectionActionModeCallback(callback)
        etName.setCustomSelectionActionModeCallback(callback)
    }

    override fun getAppContext(): Context {
        return applicationContext
    }

    private var imeNumber: String? = ""
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0
    private var encodedImage: String = ""
    lateinit var mPresenter: FormContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        ErrorHandler.toCatch(this)

        setContentView(R.layout.activity_form)

//        mPresenter = FormPresenter(this)
//
//        mPresenter.start()
//
//        ivShape.visibility = View.GONE
//
//        uiInitialization()

//        location = SimpleLocation(this)
//
//        latitude = location!!.getLatitude()
//
//        longitude = location!!.getLongitude()

        statusCheck()
    }

    private fun uiInitialization() {

        var entries1 = applicationContext.resources.getStringArray(R.array.type).toList()
        mPresenter.handleFormType(entries1[0], 0)

        spinnerType.attachDataSource(entries1)

        spinnerType.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            var entries = applicationContext.resources.getStringArray(R.array.type_real)
            mPresenter.handleFormType(entries[position], position)

            when (position) {
                0 -> {
                    ivShape.visibility = View.GONE
                    mPresenter.handleFormType("", position)
                }
                1, 2 -> {
                    ivShape.visibility = View.VISIBLE
                    ivShape.setImageResource(R.drawable.rec)
                    etDiameter.visibility = View.GONE
                    tvDiameterLabel.visibility = View.GONE

                    tvLengthLabel.visibility = View.VISIBLE
                    etLength.visibility = View.VISIBLE
                    tvWidthLabel.visibility = View.VISIBLE
                    etWidth.visibility = View.VISIBLE
                    tvHeightLabel.visibility = View.VISIBLE
                    etHeight.visibility = View.VISIBLE
                }
                3 -> {
                    ivShape.visibility = View.VISIBLE
                    ivShape.setImageResource(R.drawable.hc)

                    etDiameter.visibility = View.VISIBLE
                    tvDiameterLabel.visibility = View.VISIBLE
                    tvLengthLabel.visibility = View.VISIBLE
                    etLength.visibility = View.VISIBLE

                    tvWidthLabel.visibility = View.GONE
                    etWidth.visibility = View.GONE
                    tvHeightLabel.visibility = View.GONE
                    etHeight.visibility = View.GONE
                }
                4 -> {
                    ivShape.visibility = View.VISIBLE
                    ivShape.setImageResource(R.drawable.vc)

                    tvLengthLabel.visibility = View.GONE
                    etLength.visibility = View.GONE
                    tvWidthLabel.visibility = View.GONE
                    etWidth.visibility = View.GONE

                    tvHeightLabel.visibility = View.VISIBLE
                    etHeight.visibility = View.VISIBLE
                    etDiameter.visibility = View.VISIBLE
                    tvDiameterLabel.visibility = View.VISIBLE
                }
            }
        }

        etDiameter.visibility = View.GONE
        tvDiameterLabel.visibility = View.GONE
        tvLengthLabel.visibility = View.VISIBLE
        etLength.visibility = View.VISIBLE
        tvWidthLabel.visibility = View.VISIBLE
        etWidth.visibility = View.VISIBLE
        tvHeightLabel.visibility = View.VISIBLE
        etHeight.visibility = View.VISIBLE
        ivShape.setImageResource(R.drawable.rec)

        btnSync.setOnClickListener {
            var item = Item()
            item.IMEI = this.imeNumber.toString()
            item.name = etName.text.toString().trim()
            item.diameter = etDiameter.text.toString().trim()
            item.length = etLength.text.toString().trim()
            item.width = etWidth.text.toString().trim()
            item.height = etHeight.text.toString().trim()
            item.volume = 0.0
            item.latitude = this.latitude.toString()
            item.longitude = this.longitude.toString()
            item.imgUrlStr = etPhoto.text.toString().trim()
            item.imgUrl = encodedImage
            mPresenter.saveItemIntoDatabase(item)
        }

        btnBrowse.setOnClickListener {
            ImagePicker.with(this)
                .crop(16f, 9f)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .galleryOnly()
                .start(101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                val filePath: String? = ImagePicker.getFilePath(data)
                if (filePath != null) {
                    etPhoto.setText(filePath)
                }
                val fileUri = data?.data
                if (fileUri != null) {
                    val imageStream = fileUri?.let { contentResolver.openInputStream(it) }
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    ivPhoto.visibility = View.VISIBLE
                    ivPhoto.setImageBitmap(selectedImage)
                    encodedImage = encodeImage(selectedImage)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun encodeImage(bm: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()

        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getBitmap(path: String?, ivPhoto: ImageView) {
        try {
            var bitmap: Bitmap? = null
            val f = File(path)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888

            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            ivPhoto.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun statusCheck() {
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("No") { dialog, id ->
                Toast.makeText(this@FormActivity, "You have to enable GPS to get your location", Toast.LENGTH_LONG)
                    .show()
                dialog.cancel()
                finish()
            }
            .setOnCancelListener { dialog ->
                Toast.makeText(this@FormActivity, "You have to enable GPS to get your location", Toast.LENGTH_LONG)
                    .show()
                dialog?.cancel()
                finish()
            }
        val alert = builder.create()
        alert.show()
    }

    val callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {

        }
    }

    private var location: SimpleLocation? = null
}

private fun EditText.setCustomSelectionActionModeCallback(callback: ActionMode.Callback) {
}
