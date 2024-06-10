package com.imdevil.playground.fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.imdevil.playground.R
import com.imdevil.playground.base.LogActivity
import com.imdevil.playground.databinding.ActivityContainerBinding

class ContainerActivity : LogActivity() {

    private lateinit var binding: ActivityContainerBinding

    override fun getLogTag() = "Fragment-ContainerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            with(Runtime.getRuntime()) {
                exec("input keyevent " + KeyEvent.KEYCODE_BACK)
            }
        }

        binding.backstack.setOnClickListener {
            supportFragmentManager.logBackStack(getLogTag(), "------")
        }

        binding.add.setOnClickListener {
            supportFragmentManager.commit {
                add(R.id.container, ColorLogFragment.newInstance("add", R.color.peachpuff), "add")
            }
        }

        binding.remove.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("add")?.let { remove(it) }
            }
        }

        binding.removeWithBackStack.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("add")?.let { remove(it) }
                addToBackStack("removeWithBackStack")
            }
        }

        binding.addWithBackStack.setOnClickListener {
            supportFragmentManager.commit {
                add(
                    R.id.container,
                    ColorLogFragment.newInstance("addWithBackStack", R.color.crimson),
                    "addWithBackStack"
                )
                addToBackStack("addWithBackStack")
            }
        }

        binding.remove2.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("addWithBackStack")?.let { remove(it) }
            }
        }

        binding.removeWithBackStack2.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("addWithBackStack")?.let { remove(it) }
                addToBackStack("removeWithBackStack2")
            }
        }

        binding.replace.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.container, ColorLogFragment.newInstance("replace", R.color.greenyellow))
            }
        }

        binding.replaceWithBackStack.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.container, ColorLogFragment.newInstance("replaceWithBackStack", R.color.darkorchid))
                addToBackStack("replaceWithBackStack")
            }
        }

        binding.hide.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("add")?.let { hide(it) }
            }
        }

        binding.show.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("add")?.let { show(it) }
            }
        }

        binding.detach.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("add")?.let { detach(it) }
            }
        }

        binding.attach.setOnClickListener {
            supportFragmentManager.commit {
                supportFragmentManager.findFragmentByTag("add")?.let { attach(it) }
            }
        }

        binding.attachNew.setOnClickListener {
            supportFragmentManager.commit {
                attach(ColorLogFragment.newInstance("add_new", R.color.peachpuff))
            }
        }
    }
}

fun FragmentManager.logBackStack(tag: String, info: String) {
    for (i in 0 until backStackEntryCount) {
        Log.d(tag, "$info: ${getBackStackEntryAt(i)}")
    }
}