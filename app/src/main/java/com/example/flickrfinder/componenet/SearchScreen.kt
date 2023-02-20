package com.example.flickrfinder.componenet

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flickrfinder.ui.theme.colorRedDark
import com.example.flickrfinder.ui.theme.colorWhite
import com.example.flickrfinder.viewmodel.PhotoViewModel

@Composable
fun SearchView(
    navigationViewModel: PhotoViewModel,
    onSubmitSearch: (searchText: String) -> Unit
) {
    val context = LocalContext.current

    AutoCompleteTextView(
        modifier = Modifier.fillMaxWidth(),
        query = navigationViewModel.searchItemValue,
        queryLabel = "Search",
        onQueryChanged = { text ->
            navigationViewModel.updateSearchItem(text)
        },
        predictions = navigationViewModel.queryList,
        onEmptyClick = {
            navigationViewModel.showMessage("Search empty!", context)
        },
        onClearClick = {
            navigationViewModel.updateSearchItem("")
        },
        onDoneActionClick = {
            onSubmitSearch(navigationViewModel.searchItemValue)
        },
        onItemClick = { text ->
            navigationViewModel.updateSearchItem(text)
        }
    ) {
        Text(
            text = it,
            modifier = Modifier.padding(start = 10.dp),
            fontSize = 14.sp
        )
    }

}

@Composable
fun <T> AutoCompleteTextView(
    modifier: Modifier,
    query: String,
    queryLabel: String,
    onQueryChanged: (String) -> Unit = {},
    predictions: List<T>,
    onEmptyClick: () -> Unit = {},
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemContent: @Composable (T) -> Unit = {}
) {

    Column(modifier = Modifier.fillMaxSize()) {
        val view = LocalView.current

        QuerySearch(
            query = query,
            label = queryLabel,
            onQueryChanged = onQueryChanged,
            onEmptyClick = onEmptyClick,
            onDoneActionClick = {
                view.clearFocus()
                onDoneActionClick()
            },
            onClearClick = {
                onClearClick()
            }
        )

        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = modifier
                .heightIn(max = TextFieldDefaults.MinHeight * 6)
                .padding(top = 5.dp)
        ) {

            if (predictions.isNotEmpty()) {
                items(predictions) { prediction ->
                    Row(
                        Modifier
                            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 5.dp)
                            .fillMaxWidth()
                            .clickable {
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
    label: String,
    onEmptyClick: () -> Unit = {},
    onDoneActionClick: () -> Unit = {},
    onClearClick: () -> Unit = {},
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
                backgroundColor = colorWhite,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(24.dp),
            placeholder = {
                Text(
                    text = "Search pictures",
                    color = Color.Gray
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
                        .background(colorRedDark)
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