package com.example.movieappmad24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.movieappmad24.ui.theme.MovieAppMAD24Theme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import com.example.movieappmad24.models.Movie
import com.example.movieappmad24.models.getMovies




class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppMAD24Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val movies = remember { getMovies() }
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Text(
                                            "Movie App",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary,
                                )
                            )
                        },

                        bottomBar = {
                            BottomAppBar(
                                containerColor = Color.White,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Default.Home,
                                            contentDescription = "Home"
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Watchlist"
                                        )
                                    }
                                }

                            }
                        }

                    ) { innerPadding ->
                        ScrollContent(innerPadding, movies)
                    }
                }
            }
        }
    }
}

@Composable
fun ScrollContent(innerPadding: PaddingValues, movies: List<Movie>) {
    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        items(movies) { movie ->
            MovieRow(movie = movie)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MovieRow(movie: Movie){
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9)
            ) {

                AsyncImage(
                    model = movie.images.firstOrNull(),
                    contentDescription = "Movie Image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp)
                        .align(Alignment.TopEnd)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(0.02f))
                Text(text = movie.title)

                Spacer(modifier = Modifier.weight(1f))
                // Toggleable icon based on expansion state
                if (expanded) {
                    ContentIcon(
                        expanded = expanded,
                        onClick = { expanded = !expanded })
                } else {
                    ContentIcon(
                        expanded = expanded,
                        onClick = { expanded = !expanded })
                }
            }

            AnimatedContent(
                targetState = expanded,
                label = "Movie Details Animation",
                transitionSpec = {
                    fadeIn(animationSpec = tween(150, 150)) togetherWith
                            fadeOut(animationSpec = tween(150)) using
                            SizeTransform(clip = false) { initialSize, targetSize ->
                                if (targetState) {
                                    keyframes {
                                        // Expand vertically to target height.
                                        IntSize(initialSize.width, targetSize.height) at 150
                                        durationMillis = 300
                                    }
                                } else {
                                    keyframes {
                                        // Shrink vertically to initial height.
                                        IntSize(initialSize.width, initialSize.height) at 150
                                        durationMillis = 300
                                    }
                                }
                            }
                }
            ) { targetExpanded ->
                if (targetExpanded) {
                    Expanded(movie)
                }
            }
        }
    }
}

@Composable
fun ContentIcon(expanded: Boolean, onClick: () -> Unit) {
    Icon(
        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
        contentDescription = if (expanded) "Collapse" else "Expand",
        modifier = Modifier
            .clickable { onClick() }
            .padding(4.dp)
    )
}

@Composable
fun Expanded(movie: Movie) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Title: ${movie.title}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Year: ${movie.year}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Genre: ${movie.genre}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Director: ${movie.director}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Actors: ${movie.actors}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Plot: ${movie.plot}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            text = "Rating: ${movie.rating}",
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}



@Composable
fun MovieList() {
    val movies = remember { getMovies() }

    LazyColumn {
        items(movies.size) { index ->
            MovieRow(movie = movies[index])
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Preview
@Composable
fun DefaultPreview() {
    MovieAppMAD24Theme {
        MovieList()
    }
}


