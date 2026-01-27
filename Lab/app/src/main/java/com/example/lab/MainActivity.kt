import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.DarkGray)

            ) {
                //HP
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(color = Color.White)

                ) {
                    Text(
                        text = "HP",
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth(fraction = 0.5f)
                            .background(Color.Red)
                            .padding(8.dp),
                    )
                }
            }
        }
    }
}