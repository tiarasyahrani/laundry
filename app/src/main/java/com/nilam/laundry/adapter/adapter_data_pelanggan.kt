package com.nilam.laundry.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.nilam.laundry.R
import com.nilam.laundry.pelanggan.TambahPelanggan
import com.nilam.laundry.adapter.adapter_data_pelanggan.ViewHolder
import com.nilam.laundry.modeldata.modelpelanggan
import org.w3c.dom.Text

class adapter_data_pelanggan (
    private val listPelanggan: ArrayList<modelpelanggan>) :
    RecyclerView.Adapter<adapter_data_pelanggan.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pelanggann, parent,false)
        appContext = parent.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: adapter_data_pelanggan.ViewHolder, position: Int) {

        val item = listPelanggan[position]
        holder.id_pelanggan.text = item.id_pelanggan ?: ""
        holder.tv_nama.text = item.tv_nama_pelanggan ?: ""
        holder.tv_nohp.text = item.tv_nohp_pelanggan ?: ""
        holder.tv_terdaftar.text =item.tv_terdaftar_pelanggan ?: ""
        holder.tv_alamat.text = item.tv_alamat_pelanggan ?: ""

        holder.bt_hubungi.setOnClickListener{
            val nomorHp = item.tv_nohp_pelanggan
            nomorHp?.let { it1 -> showHubungiDialog(holder.itemView.context,it1)}
        }

        holder.tv_card_pelanggan.setOnClickListener {
            val intent = Intent (appContext, TambahPelanggan::class.java)
            intent.putExtra("judul", "Edit Pelanggan")
            intent.putExtra("id", item.id_pelanggan )
            intent.putExtra("namaPegawai", item.tv_nama_pelanggan )
            intent.putExtra("noHPPegawai", item.tv_nohp_pelanggan )
            intent.putExtra("alamatPegawai", item.tv_alamat_pelanggan )
            appContext.startActivity(intent)

        }
        holder.bt_lihat.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.dialog_mod_pelanggan, null)

            val dialog = android.app.AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .create()

            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_id_isi).text = item.id_pelanggan
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_namapelanggan_isi).text = item.tv_nama_pelanggan
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_alamat_isi).text = item.tv_alamat_pelanggan
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_NOHP_isi).text = item.tv_nohp_pelanggan
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pelanggan_terdaftar_isi).text = item.tv_terdaftar_pelanggan

            dialogView.findViewById<Button>(R.id.buttonsunting).setOnClickListener {
                val intent = Intent(holder.itemView.context, TambahPelanggan::class.java)
                intent.putExtra("judul", "edit")
                intent.putExtra("id", item.id_pelanggan)
                intent.putExtra("nama", item.tv_nama_pelanggan)
                intent.putExtra("alamat", item.tv_alamat_pelanggan)
                intent.putExtra("terdaftar", item.tv_terdaftar_pelanggan)
                intent.putExtra("nohp", item.tv_nohp_pelanggan)
                holder.itemView.context.startActivity(intent)
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.buttonhapus).setOnClickListener {
                android.app.AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Hapus pelanggan?")
                    .setMessage("Data pelanggan akan dihapus.")
                    .setPositiveButton("Hapus") { _, _ ->
                        // Hapus dari Firebase
                        val db = com.google.firebase.database.FirebaseDatabase.getInstance()
                            .getReference("pelanggan")
                        item.id_pelanggan?.let { it1 ->
                            db.child(it1).removeValue().addOnSuccessListener {
                                android.widget.Toast.makeText(holder.itemView.context, "Data dihapus", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        dialog.dismiss()
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }

            dialog.show()
        }


    }

    override fun getItemCount(): Int {
        return listPelanggan.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_card_pelanggan: CardView = itemView.findViewById(R.id.tv_card_pelanggan)
        val id_pelanggan: TextView = itemView.findViewById(R.id.id_pelanggan)
        val tv_nama: TextView = itemView.findViewById(R.id.tv_nama_pelanggan)
        val tv_nohp: TextView = itemView.findViewById(R.id.tv_nohp_pelanggan)
        val tv_alamat: TextView = itemView.findViewById(R.id.tv_alamat_pelanggan)
        val tv_terdaftar: TextView = itemView.findViewById(R.id.tv_terdaftar_pelanggan)
        val bt_hubungi: Button = itemView.findViewById(R.id.bt_hubungipelanggan)
        val bt_lihat: Button = itemView.findViewById(R.id.bt_lihatpelanggan)
    }

    private fun showHubungiDialog(context: Context, phoneNumber: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mod_hubungi_pelanggan, null)
        val dialog = android.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        val btnWhatsapp = dialogView.findViewById<Button>(R.id.buttonWA_hubungi)
        val btnTelepon = dialogView.findViewById<Button>(R.id.buttonTelp_hubungi)
        val btnBatal = dialogView.findViewById<TextView>(R.id.buttonBatal_hubungi)

        btnWhatsapp.setOnClickListener {
            val formattedNumber = if (phoneNumber.startsWith("0")) {
                "+62" + phoneNumber.substring(1)
            } else {
                phoneNumber
            }
            val url = "https://wa.me/$formattedNumber"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            context.startActivity(intent)
            dialog.dismiss()

        }
        btnTelepon.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = android.net.Uri.parse("tel:$phoneNumber")
            context.startActivity(intent)
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

}

}