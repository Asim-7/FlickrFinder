package com.example.flickrfinder.components.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.ui.theme.colorWhite
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun SearchView(
    navigationViewModel: PhotoViewModel,
    onSubmitSearch: (searchText: String) -> Unit
) {
    val uiState by navigationViewModel.uiState.collectAsState()

    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = uiState.searchItemValue,
        onQueryChanged = { text ->
            navigationViewModel.updateSearchQuery(text)
        },
        predictions = uiState.queryList,
        onEmptyClick = {
            navigationViewModel.showMessage("Search empty!")
        },
        onDoneActionClick = {
            onSubmitSearch(uiState.searchItemValue)
        },
        onItemClick = { text ->
            navigationViewModel.updateSearchQuery(text)
        }
    ) {
        Row(modifier = Modifier.wrapContentSize()) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "",
                tint = MaterialTheme.colors.onSurface
            )
            Text(
                text = it,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface
            )
        }
    }

}

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onEmptyClick: () -> Unit = {},
    onDoneActionClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {

    Column(modifier = Modifier.fillMaxSize()) {
        val view = LocalView.current

        QuerySearch(
            query = query,
            onQueryChanged = onQueryChanged,
            onEmptyClick = onEmptyClick,
            onDoneActionClick = {
                view.clearFocus()
                onDoneActionClick()
            },
        )

        val interactionSource = remember { MutableInteractionSource() }
        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .heightIn(max = TextFieldDefaults.MinHeight * 6)
                .padding(top = 5.dp, start = 5.dp, end = 5.dp)
        ) {

            if (predictions.isNotEmpty()) {
                items(predictions) { prediction ->
                    Row(
                        Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 5.dp)
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                onItemClick(prediction)
                            }
                    ) {
                        itemContent(prediction)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun QuerySearch(
    modifier: Modifier = Modifier,
    query: String,
    onEmptyClick: () -> Unit = {},
    onDoneActionClick: () -> Unit = {},
    onQueryChanged: (String) -> Unit
) {

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val maxChar = 15

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(24.dp)
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.primary,
                backgroundColor = MaterialTheme.colors.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(24.dp),
            placeholder = {
                Text(
                    text = "Search pictures",
                    color = MaterialTheme.colors.onSurface
                )
            },
            value = query,
            onValueChange = {
                val allowedString = it.isAllowed()
                if (allowedString.length <= maxChar) onQueryChanged(allowedString)
            },
            textStyle = MaterialTheme.typography.subtitle1,
            singleLine = true,
            trailingIcon = {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colors.primary)
                ) {
                    IconButton(onClick = {
                        if (query.isEmpty()) {
                            onEmptyClick()
                        } else {
                            focusRequester.freeFocus()
                            keyboardController?.hide()
                            Handler(Looper.getMainLooper()).postDelayed({       // handler added to hide keyboard correctly
                                onDoneActionClick()
                            }, 0)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = colorWhite
                        )
                    }
                }
            },
            keyboardActions = KeyboardActions(onDone = {
                if (query.isEmpty()) {
                    onEmptyClick()
                } else {
                    focusRequester.freeFocus()
                    keyboardController?.hide()
                    Handler(Looper.getMainLooper()).postDelayed({       // handler added to hide keyboard correctly
                        onDoneActionClick()
                    }, 0)
                }
            }),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            )
        )
    }

}

private fun String.isAllowed() = filter { it.isLetter() }