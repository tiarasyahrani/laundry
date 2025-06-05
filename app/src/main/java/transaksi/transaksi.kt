    package transaksi

    import android.content.Intent
    import android.content.SharedPreferences
    import android.os.Bundle
    import android.view.View
    import android.widget.Button
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.google.firebase.FirebaseApp
    import com.nilam.laundry.Pilih_Pelanggan
    import com.nilam.laundry.R
    import com.nilam.laundry.activity_pilih_layanan
    import com.nilam.laundry.adapter.adapter_tambahan_dipilih
    import com.nilam.laundry.konfirmasi
    import com.nilam.laundry.modeldata.modelTransaksiTambahan
    import com.nilam.laundry.modeldata.model_tambahkan
    import com.nilam.laundry.pilih_tambahan

    class transaksi : AppCompatActivity() {
        private lateinit var btnPilihPelanggan: Button
        private lateinit var btnPilihLayanan: Button
        private lateinit var btnTambahan: Button
        private lateinit var btnProses: Button
        private lateinit var tvTRANSAKSI_DATA_pelanggan_nama: TextView
        private lateinit var tvTRANSAKSI_DATA_pelanggan_nohp: TextView
        private lateinit var tvTRANSAKSI_layanan_nama: TextView
        private lateinit var tvTRANSAKSI_layanan_harga: TextView
        private lateinit var rvTRANSAKSI_LayananTambahan: RecyclerView

        private lateinit var adapterTambahan: adapter_tambahan_dipilih
        private val dataList = mutableListOf<modelTransaksiTambahan>()

        private val pilihPelanggan = 1
        private val pilihLayanan = 2
        private val pilihLayananTambahan = 3

        private var idPelanggan: String = ""
        private var idCabang: String = ""
        private var namaPelanggan: String = ""
        private var noHP: String = ""
        private var idLayanan: String = ""
        private var namaLayanan: String = ""
        private var hargaLayanan: String = ""
        private var idPegawai: String = ""

        private lateinit var sharedPref: SharedPreferences
        private val listTambahan = mutableListOf<model_tambahkan>()



        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_transaksi)
            sharedPref = getSharedPreferences("user_data", MODE_PRIVATE)
            idCabang = sharedPref.getString("idCabang", null).toString()
            idPegawai = sharedPref.getString("idPegawai", null).toString()
            init()
            FirebaseApp.initializeApp(this)
            val layoutManager = LinearLayoutManager(this)
            layoutManager.reverseLayout = false
            rvTRANSAKSI_LayananTambahan.layoutManager = layoutManager
            rvTRANSAKSI_LayananTambahan.setHasFixedSize(true)

            adapterTambahan = adapter_tambahan_dipilih(listTambahan) { tambahan ->
                listTambahan.remove(tambahan)
                adapterTambahan.notifyDataSetChanged()
            }
            rvTRANSAKSI_LayananTambahan.adapter = adapterTambahan

            btnPilihPelanggan.setOnClickListener {
                val intent = Intent(this, Pilih_Pelanggan::class.java)
                startActivityForResult(intent, pilihPelanggan)
            }
            btnPilihLayanan.setOnClickListener {
                val intent = Intent(this, activity_pilih_layanan::class.java)
                startActivityForResult(intent, pilihLayanan)
            }
            btnTambahan.setOnClickListener{
                val intent = Intent(this, pilih_tambahan::class.java)
                startActivityForResult(intent, pilihLayananTambahan)
            }

            val btnProsesTransaksi: Button = findViewById(R.id.btnProses)
            btnProsesTransaksi.setOnClickListener {
                if (namaPelanggan.isEmpty() || namaLayanan.isEmpty()) {
                    Toast.makeText(this, getString(R.string.toast_Lengkapidatapelanggandanlayanan), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val intent = Intent(this, konfirmasi::class.java)
                intent.putExtra("namaPelanggan", namaPelanggan)
                intent.putExtra("noHp", noHP)
                intent.putExtra("layananUtama", namaLayanan)
                val hargaUtamaFix = hargaLayanan
                    .replace(".", "")
                    .replace(",", "")
                    .replace("Rp", "")
                    .replace(" ", "")
                    .toIntOrNull() ?: 0
                intent.putExtra("hargaUtama", hargaUtamaFix)



                val tambahanStrings = ArrayList<String>()
                for (tambahan in listTambahan) {
                    tambahanStrings.add("${tambahan.idTambahan}|${tambahan.namaTambahan}|${tambahan.harga}")

                }
                intent.putStringArrayListExtra("layananTambahan", tambahanStrings)

                startActivity(intent)
            }


            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
        fun init () {
            btnPilihPelanggan = findViewById(R.id.btnPilihPelanggan)
            btnPilihLayanan = findViewById(R.id.btnPilihLayanan)
            btnProses = findViewById(R.id.btnProses)
            tvTRANSAKSI_DATA_pelanggan_nama = findViewById(R.id.tvTRANSAKSI_DATA_pelanggan_nama)
            tvTRANSAKSI_DATA_pelanggan_nohp = findViewById(R.id.tvTRANSAKSI_DATA_pelanggan_nohp)
            tvTRANSAKSI_layanan_nama = findViewById(R.id.tvTRANSAKSI_layanan_nama)
            tvTRANSAKSI_layanan_harga = findViewById(R.id.tvTRANSAKSI_layanan_harga)
            rvTRANSAKSI_LayananTambahan = findViewById(R.id.rvTRANSAKSI_LayananTambahan)
            btnTambahan = findViewById(R.id.btnTambah)


            tvTRANSAKSI_DATA_pelanggan_nama.text = "Nama Pelanggan : -"
            tvTRANSAKSI_DATA_pelanggan_nohp.text = "No HP : -"
            tvTRANSAKSI_layanan_nama.text = "Nama Layanan : -"
            tvTRANSAKSI_layanan_harga.text = "Harga : -"
        }

        @Deprecated("This method has been deprecated in favor of using the Activity Result")
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == pilihPelanggan){
                if (resultCode == RESULT_OK && data !=null){
                    idPelanggan = data.getStringExtra("idPelanggan").toString()
                    val nama = data.getStringExtra("nama")
                    val nomorHP = data.getStringExtra("noHP")
                    tvTRANSAKSI_DATA_pelanggan_nama.text = "Nama Pelanggan : $nama"
                    tvTRANSAKSI_DATA_pelanggan_nohp.text = "No HP : $nomorHP"

                    namaPelanggan = nama.toString()
                    noHP = nomorHP.toString()

                    Toast.makeText(this, "Pelanggan Dipilih : $namaPelanggan",Toast.LENGTH_SHORT).show()

                }
                if (resultCode == RESULT_CANCELED){
                    Toast.makeText(this, getString(R.string.toast_BatalMemilihPelanggan),Toast.LENGTH_SHORT).show()
                }
            }
            if (requestCode == pilihLayanan){
                if (resultCode == RESULT_OK && data !=null){
                    idLayanan = data.getStringExtra("idLayanan").toString()
                    val layanan = data.getStringExtra("nama")
                    val harga = data.getStringExtra("harga")
                    tvTRANSAKSI_layanan_nama.text = "Nama Layanan : $layanan"
                    tvTRANSAKSI_layanan_harga.text = "Harga : Rp$harga"

                    namaLayanan = layanan.toString()
                    hargaLayanan = harga.toString()

                    Toast.makeText(this, "Layanan Dipilih : $namaLayanan",Toast.LENGTH_SHORT).show()

                }
                if (resultCode == RESULT_CANCELED){
                    Toast.makeText(this, getString(R.string.toast_BatalMemilihLayanan),Toast.LENGTH_SHORT).show()
                }

            }
            if (requestCode == pilihLayananTambahan && resultCode == RESULT_OK) {
                val id = data?.getStringExtra("id") ?: return
                val nama = data.getStringExtra("nama") ?: return
                val harga = data.getStringExtra("harga") ?: return


                val tambahan = model_tambahkan(id, nama, harga)

                // Cek apakah sudah ada item dengan ID yang sama
                val sudahAda = listTambahan.any { it.idTambahan == id }
                if (!sudahAda) {
                    listTambahan.add(tambahan)
                    adapterTambahan.notifyDataSetChanged()
                    rvTRANSAKSI_LayananTambahan.visibility = View.VISIBLE


                    Toast.makeText(this, "Tambahan ditambahkan: $nama", Toast.LENGTH_SHORT).show()


                } else {
                    Toast.makeText(this, getString(R.string.toast_Layanantambahansudahada), Toast.LENGTH_SHORT).show()
                }


            }


        }

    }

