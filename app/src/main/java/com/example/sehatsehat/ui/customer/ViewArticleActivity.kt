package com.example.sehatsehat.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sehatsehat.data.sources.remote.ArticleJSON
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.ui.customer.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.ViewArticleViewModel
import com.google.gson.Gson

class ViewArticleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val title = intent.getStringExtra("title") ?: "No Title"
        val description = intent.getStringExtra("description") ?: "No Description"
        val content = intent.getStringExtra("content") ?: "No Content"
        val author = intent.getStringExtra("author") ?: "Unknown Author"
        val date = intent.getStringExtra("date") ?: "Unknown Date"

        val article = Article(
            title = title,
            description = description,
            content = content,
            author = author,
            date = date
        )

        setContent {
            SehatSehatTheme {
                ArticleDetailScreen(article = article)
            }
        }
    }
}

@Composable
fun ArticleDetailScreen(article: Article) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = article.title, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "By ${article.author}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = article.date.split("T")[0], style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = article.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = article.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
