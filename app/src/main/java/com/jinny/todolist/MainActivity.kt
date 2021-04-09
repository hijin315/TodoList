package com.jinny.todolist

import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jinny.todolist.databinding.ActivityMainBinding
import com.jinny.todolist.databinding.ItemTodoBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel : TodoAdapter.MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.rvItem.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = TodoAdapter(viewModel.data, onClickDelete = {
                viewModel.deleteTodo(it)

            }, onClickItem = {
                viewModel.toggleTodo(it)
            })
        }

        binding.addBtn.setOnClickListener {
            val todo = Todo(binding.addEt.text.toString())
            viewModel.addTodo(todo)
            binding.rvItem.adapter?.notifyDataSetChanged()
        }
    }

//    private fun toggleTodo(todo: Todo) {
//        todo.isDone = !todo.isDone
//        binding.rvItem.adapter?.notifyDataSetChanged()
//    }
//
//    private fun addTodo() {
//        val todoText = binding.addEt.text.toString()
//        data.add(Todo(todoText))
//        binding.rvItem.adapter?.notifyDataSetChanged()
//    }
//
//    private fun deleteTodo(todo: Todo) {
//        data.remove(todo)
//        binding.rvItem.adapter?.notifyDataSetChanged()
//    }
}

data class Todo(
    val text: String,
    var isDone: Boolean = false
) {
    //자동으로 게터세터가 생성된 dataclass ~!~!
}

class TodoAdapter(
    private val MyDataset: List<Todo>,
    val onClickDelete: (todo: Todo) -> Unit,
    val onClickItem: (todo: Todo) -> Unit
) :
    RecyclerView.Adapter<TodoAdapter.TodoListHolder>() {
    class TodoListHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)
    //.root 하면 본인이 어떤 뷰로 부터 생성된 바인딩인지 확인 가능

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.TodoListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return TodoListHolder(ItemTodoBinding.bind(view))
    }

    override fun getItemCount(): Int = MyDataset.size

    override fun onBindViewHolder(holder: TodoListHolder, position: Int) {
        val todo = MyDataset[position]
        holder.binding.tvList.text = todo.text
        if (todo.isDone) {
            //holder.binding.tvList.paintFlags = holder.binding.tvList.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            //이렇게 줄일 수 있음
            holder.binding.tvList.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.ITALIC)
            }
        } else {
            holder.binding.tvList.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }
        holder.binding.ivDelete.setOnClickListener { onClickDelete.invoke(todo) }
        holder.binding.root.setOnClickListener { onClickItem.invoke(todo) }
    }

    class MainViewModel : ViewModel() {
        val data = arrayListOf<Todo>()

        fun toggleTodo(todo: Todo) {
            todo.isDone = !todo.isDone
        }

        fun addTodo(todo: Todo) {
            data.add(todo)
        }

        fun deleteTodo(todo: Todo) {
            data.remove(todo)
        }
    }
}