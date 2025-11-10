package com.rovits.app.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TermsDialog(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kullanım Koşulları ve Gizlilik Politikası") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = """
                        KULLANIM KOŞULLARI
                        
                        1. Genel Hükümler
                        Bu uygulama, POI (Points of Interest) verilerini yönetmek için kullanıcılara hizmet sunmaktadır. Uygulamayı kullanarak aşağıdaki koşulları kabul etmiş sayılırsınız.
                        
                        2. Kullanıcı Yükümlülükleri
                        - Doğru ve güncel bilgiler sağlamak
                        - Hesap güvenliğini korumak
                        - Yasal olmayan faaliyetlerde kullanmamak
                        - Diğer kullanıcıların haklarına saygı göstermek
                        
                        3. Hizmet Kullanımı
                        - Hizmet "olduğu gibi" sunulmaktadır
                        - Kesintisiz hizmet garantisi verilmemektedir
                        - İçerik ve özellikler değiştirilebilir
                        
                        GİZLİLİK POLİTİKASI
                        
                        1. Toplanan Bilgiler
                        - Ad, soyad ve email adresi
                        - Konum bilgileri (izninizle)
                        - Kullanım istatistikleri
                        
                        2. Bilgi Kullanımı
                        Topladığımız bilgiler şu amaçlarla kullanılır:
                        - Hizmet sağlamak ve geliştirmek
                        - Kullanıcı deneyimini iyileştirmek
                        - İletişim kurmak
                        
                        3. Bilgi Güvenliği
                        Verileriniz endüstri standardı güvenlik önlemleriyle korunmaktadır. Şifreler hash'lenerek saklanır ve üçüncü taraflarla paylaşılmaz.
                        
                        4. Kullanıcı Hakları
                        - Verilerinize erişim talep edebilirsiniz
                        - Verilerin düzeltilmesini isteyebilirsiniz
                        - Hesabınızı ve verilerinizi silebilirsiniz
                        
                        5. İletişim
                        Sorularınız için: support@rovitspoi.com
                        
                        Son güncelleme: 10 Kasım 2025
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onAccept) {
                Text("Kabul Ediyorum")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

