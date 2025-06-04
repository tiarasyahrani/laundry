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
import com.nilam.laundry.TambahPegawai
import com.nilam.laundry.modeldata.modelpegawai

class adapter_data_pegawai (private val listPegawai: ArrayList<modelpegawai>) :
    RecyclerView.Adapter<adapter_data_pegawai.ViewHolder>() {
    lateinit var appContext: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_data_pegawai, parent,false)
        appContext = parent.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= listPegawai[position]
        holder.id_pegawai.text = item.id_pegawai ?: ""
        holder.tv_namapegawai.text = item.namaPegawai ?: ""
        holder.tv_alamatpegawai.text = item.alamatPegawai ?: ""
        holder.tv_nohppegawai.text = item.noHPPegawai ?: ""
        holder.tv_cabangpegawai.text = item.cabangPegawai ?: ""
        holder.tv_terdaftarpegawai.text = item.terdaftarPegawai ?: ""


        holder.bthubungipegawai.setOnClickListener{
            val nomorHp = item.noHPPegawai
            nomorHp?.let { it1 -> showHubungiDialog(holder.itemView.context,it1)}
        }
        holder.btlihatpegawai.setOnClickListener {
            val dialogView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.dialog_mod_pegawai, null)

            val dialog = android.app.AlertDialog.Builder(holder.itemView.context)
                .setView(dialogView)
                .create()

            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_id_isi).text = item.id_pegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_namapelanggan_isi).text = item.namaPegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_alamat_isi).text = item.alamatPegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_NOHP_isi).text = item.noHPPegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_terdaftar_isi).text = item.terdaftarPegawai
            dialogView.findViewById<TextView>(R.id.tv_dialog_mod_pegawai_cabang_isi).text = item.cabangPegawai


            // Tombol Edit
            dialogView.findViewById<Button>(R.id.buttonsunting_pegawai).setOnClickListener {
                val intent = Intent(holder.itemView.context, TambahPegawai::class.java)
                intent.putExtra("judul", "edit")
                intent.putExtra("id", item.id_pegawai)
                intent.putExtra("nama", item.namaPegawai)
                intent.putExtra("alamat", item.alamatPegawai)
                intent.putExtra("terdaftar", item.terdaftarPegawai)
                intent.putExtra("cabang", item.cabangPegawai)
                holder.itemView.context.startActivity(intent)
                dialog.dismiss()
            }

            // Tombol Hapus
            dialogView.findViewById<Button>(R.id.buttonhapus_pegawai).setOnClickListener {
                android.app.AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Hapus Pegawai?")
                    .setMessage("Data Pegawai akan dihapus.")
                    .setPositiveButton("Hapus") { _, _ ->
                        // Hapus dari Firebase
                        val db = com.google.firebase.database.FirebaseDatabase.getInstance()
                            .getReference("pegawai")
                        item.id_pegawai?.let { it1 ->
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

        holder.tv_card_pegawai.setOnClickListener {
            val intent = Intent (appContext, TambahPegawai::class.java)
            intent.putExtra("judul", "Edit Pegawai")
            intent.putExtra("id", item.id_pegawai )
            intent.putExtra("namaPegawai", item.namaPegawai )
            intent.putExtra("noHPPegawai", item.noHPPegawai )
            intent.putExtra("alamatPegawai", item.alamatPegawai )
            intent.putExtra("idCabang", item. cabangPegawai)
            appContext.startActivity(intent)



        }
    }
    override fun getItemCount(): Int {
        return listPegawai.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_card_pegawai: CardView = itemView.findViewById(R.id.tv_card_pegawai)
        val id_pegawai: TextView = itemView.findViewById(R.id.id_pegawai)
        val tv_namapegawai: TextView = itemView.findViewById(R.id.tv_namapegawai)
        val tv_alamatpegawai: TextView = itemView.findViewById(R.id.tv_alamatpegawai)
        val tv_nohppegawai: TextView = itemView.findViewById(R.id.tv_nohppegawai)
        val tv_terdaftarpegawai: TextView = itemView.findViewById(R.id.tv_terdaftarpegawai)
        val tv_cabangpegawai: TextView = itemView.findViewById(R.id.tv_cabangpegawai)
        val bthubungipegawai: Button = itemView.findViewById(R.id.bt_hubungipegawai)
        val btlihatpegawai: Button = itemView.findViewById(R.id.bt_lihatpegawai)
    }

    private fun showHubungiDialog(context: Context, phoneNumber: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_mod_hubungi_pegawai, null)
        val dialog = android.app.AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        val btnWhatsapp = dialogView.findViewById<Button>(R.id.buttonWA_hubungi_pegawai)
        val btnTelepon = dialogView.findViewById<Button>(R.id.buttonTelp_hubungi_pegawai)
        val btnBatal = dialogView.findViewById<TextView>(R.id.buttonBatal_hubungi_pegawai)

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