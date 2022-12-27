# Halo semua! 

Project kali ini bagian dari tugas Submission SIB Dicoding bernama **Aplikasi Story App** submission 1.\
**Apliksi Story App** adalah aplikasi android untuk setiap pengguna dapat bertukar cerita setiap momennya 
dalam bentuk gambar yang diambil ataupun galeri. 

Gambaran Sistem :
Sistem Informasi ini terdiri dari proses :
Fitur yang harus ada pada aplikasi.

- Halaman Autentikasi
    * Syarat yang harus dipenuhi sebagai berikut.

        a. Menampilkan halaman login untuk masuk ke dalam aplikasi. Berikut input yang dibutuhkan.

            - Email (R.id.ed_login_email)

            - Password (R.id.ed_login_password)

        b. Membuat halaman register untuk mendaftarkan diri dalam aplikasi. Berikut input yang dibutuhkan.

            Nama (R.id.ed_register_name)

            Email (R.id.ed_register_email)

            Password (R.id.ed_register_password)
            
        c. Untuk password wajib disembunyikan.

        d. Membuat Custom View berupa EditText pada halaman login dan register dengan ketentuan sebagai berikut.

            - Jika jumlah password kurang dari 6 karakter, menampilkan error langsung pada EditText.

        e. Menyimpan data sesi dan token di preferences. Data sesi digunakan untuk mengatur alur aplikasi dengan spesifikasi seperti berikut.

            - Jika sudah login langsung masuk ke halaman utama.

            - Jika belum maka akan masuk ke halaman login. 

        f. Terdapat fitur untuk logout (R.id.action_logout) pada halaman utama dengan ketentuan sebagai berikut.

            - Ketika tombol logout ditekan, informasi token, dan sesi harus dihapus.

- Daftar Cerita (List Story)
    * Syarat yang harus dipenuhi sebagai berikut.

        a. Menampilkan daftar cerita dari API yang disediakan. Berikut minimal informasi yang wajib Anda tampilkan.

            - Nama user (R.id.tv_item_name)

            - Foto  (R.id.iv_item_photo)

        b. Muncul halaman detail ketika salah satu item cerita ditekan. Berikut  minimal informasi yang wajib Anda tampilkan.

            - Nama user (R.id.tv_detail_name)

            - Foto (R.id.iv_detail_photo)

            - Deskripsi (R.id.tv_detail_description)

- Tambah Cerita
    * Syarat yang harus dipenuhi sebagai berikut.

        a. Membuat halaman untuk menambah cerita baru yang dapat diakses dari halaman daftar cerita. Berikut input minimal yang dibutuhkan.

            - File foto (bisa dari galeri dan kamera)

            - Deskripsi cerita (R.id.ed_add_description)

        b. Berikut adalah ketentuan dalam menambahkan cerita baru:

            - Terdapat tombol (R.id.button_add) untuk upload data ke server. 

            - Setelah tombol tersebut diklik dan proses upload berhasil, maka akan kembali ke halaman daftar cerita. 

            - Data cerita terbaru harus muncul di paling atas.

- Menampilkan Animasi
    * Syarat yang harus dipenuhi sebagai berikut.

        a. Membuat animasi pada aplikasi dengan menggunakan salah satu jenis animasi (menuliskan jenis dan lokasi animasi pada Student Note).

            - Motion Animation

            - Shared Element

Proses pengembangan :
- Kotlin

Dikerjakan oleh: 
- Ari Lathifatul Chusna (saya)
