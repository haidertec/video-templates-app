package com.videotemplate.app

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var categoryChipGroup: ChipGroup
    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: TemplateAdapter
    private val allTemplates = buildTemplates()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.searchEditText)
        categoryChipGroup = findViewById(R.id.categoryChipGroup)
        recyclerView = findViewById(R.id.recyclerView)

        adapter = TemplateAdapter { template ->
            copyToClipboard(template)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        setupCategories()
        setupSearch()
        filterTemplates()
    }

    private fun setupCategories() {
        val categories = listOf("All", "Business", "Motivation", "Tech", "Education", "Lifestyle", "Marketing", "Fitness", "Gaming", "Social")
        categories.forEach { category ->
            val chip = Chip(this)
            chip.text = category
            chip.isCheckable = true
            if (category == "All") {
                chip.isChecked = true
            }
            chip.setOnClickListener {
                filterTemplates()
            }
            categoryChipGroup.addView(chip)
        }
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterTemplates()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterTemplates() {
        val selectedCategory = getSelectedCategory()
        val query = searchEditText.text.toString().trim()

        val filtered = allTemplates.filter { template ->
            val matchesCategory = selectedCategory == "All" || template.category == selectedCategory
            val matchesQuery = query.isEmpty() ||
                template.title.contains(query, ignoreCase = true) ||
                template.content.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }

        adapter.submitList(filtered)
    }

    private fun getSelectedCategory(): String {
        val checkedChipId = categoryChipGroup.checkedChipId
        val checkedChip = categoryChipGroup.findViewById<Chip>(checkedChipId)
        return checkedChip?.text?.toString() ?: "All"
    }

    private fun copyToClipboard(template: TemplateItem) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("video_template", template.content)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Template copied", Toast.LENGTH_SHORT).show()
    }

    private fun buildTemplates(): List<TemplateItem> {
        return listOf(
            TemplateItem("1", "I Tried This for 7 Days… Here’s What Happened", "Business", "I tested this strategy for 7 days and tracked everything. The first few days were confusing, but by day 4 I started noticing real results. Here’s what happened, what I learned, and what I would do differently."),
            TemplateItem("2", "3 Mistakes I Made When Starting [Topic]", "Business", "When I first started, I made these mistakes: lack of consistency, unclear goals, and not tracking progress. If you’re beginning, avoid these and you’ll save a lot of time and frustration."),
            TemplateItem("3", "POV: You’re Trying to [Goal] for the First Time", "Motivation", "POV: you’re trying to grow your audience from zero. You’re confused, overwhelmed, and not sure where to start. But the truth is, every expert was once a beginner. Start small, stay consistent, and keep learning."),
            TemplateItem("4", "Before vs After: My [Transformation]", "Lifestyle", "Here’s my before and after journey. In the beginning, I felt stuck and discouraged, but once I changed my routine, my results improved dramatically. This is the progress I didn’t expect to happen so quickly."),
            TemplateItem("5", "My Honest Review of [Product/Tool/Service]", "Tech", "I’ve used this tool for a while now, and here’s the honest breakdown: what works, what doesn’t, and whether I’d recommend it. If you’re thinking about buying it, this review will help you decide."),
            TemplateItem("6", "What I Wish I Knew Before [Action]", "Education", "Looking back, there are a few things I wish I understood before starting. I thought it would be harder, but the biggest challenge was actually consistency. This is what I’d tell myself from day one."),
            TemplateItem("7", "The Easiest Way to [Outcome]", "Marketing", "If you want [Outcome], start with the simplest version of the process. Don’t complicate it. Focus on one clear step at a time, keep it realistic, and improve only after you begin seeing momentum."),
            TemplateItem("8", "Stop Doing This if You Want [Result]", "Motivation", "If you want better results, stop doing this one thing: waiting for perfection. Most people get stuck because they want everything to be perfect before they start. Start imperfectly and improve as you go."),
            TemplateItem("9", "This Is Why [Problem] Happens", "Education", "This problem happens because most people focus on the symptom instead of the root cause. Once you understand the real trigger, you can fix it faster and more effectively."),
            TemplateItem("10", "How I Got [Result] in [Time Period]", "Business", "This is exactly how I got results in [Time Period]. I focused on one strategy, measured the outcome, and adjusted quickly. The key wasn’t luck — it was consistency and learning from feedback."),
            TemplateItem("11", "A Day in the Life of a [Profession]", "Lifestyle", "Here’s what a typical day looks like for me as a [Profession]. It’s not always glamorous, but it’s structured, focused, and built around priorities that actually move the work forward."),
            TemplateItem("12", "Things I Do Every Morning to [Goal]", "Lifestyle", "My morning routine is simple but powerful. I start with my priorities, review my goals, and avoid distractions before my workday begins. That small routine helps me stay productive and focused."),
            TemplateItem("13", "How I Built [Thing] from Scratch", "Business", "This is how I built [Thing] from nothing. I started with a simple idea, made the first version quickly, tested it, and improved it over time. The most important step was actually launching."),
            TemplateItem("14", "The Secret to [Result] Nobody Talks About", "Marketing", "The real secret isn’t complexity — it’s clarity. Most people overcomplicate the process. If you can simplify your offer, message, and action plan, you’ll see much faster progress."),
            TemplateItem("15", "5 Things I Learned From [Experience]", "Education", "Here are 5 lessons I learned from this experience. Some were hard, some were surprising, and all of them helped me improve. If you’re going through something similar, I hope this helps."),
            TemplateItem("16", "I Tested [Method] for [Time], Here’s the Result", "Tech", "I tested this method for [Time], and here’s the outcome. The results were better than expected in some areas and weaker in others. This is what I’d recommend based on actual experience."),
            TemplateItem("17", "This Tool Changed My Workflow", "Tech", "This tool improved the way I work by saving time, reducing stress, and making tasks easier to manage. If you’re trying to improve your workflow, this is worth a look."),
            TemplateItem("18", "My Top 10 Tips for [Topic]", "Education", "Here are my top 10 tips for [Topic]. These are the things I’ve found most useful in real life, and I wish I had learned them sooner."),
            TemplateItem("19", "6 Habits That Help Me [Outcome]", "Lifestyle", "These are the habits I practice regularly to stay productive and focused. They’re simple, realistic, and easy to repeat even on busy days."),
            TemplateItem("20", "The Worst Advice About [Topic]", "Education", "The worst advice about [Topic] is thinking you need to do everything at once. Real progress comes from focusing on the right things, not doing everything at maximum speed."),
            TemplateItem("21", "What No One Tells You About [Topic]", "Business", "Most people don’t tell you how difficult it is to stay consistent. The real work isn’t just the task itself — it’s the discipline behind it. That’s what makes the difference."),
            TemplateItem("22", "How to Start [Goal] in 10 Minutes", "Motivation", "Starting is usually the hardest part. Here’s a simple 10-minute plan to begin immediately, remove the stress, and get moving without overthinking."),
            TemplateItem("23", "One Thing That Changed My [Situation]", "Motivation", "One small change transformed the way I approached this. It wasn’t the biggest thing — it was the most consistent one. And that’s often how real improvement begins."),
            TemplateItem("24", "How I Make [Income/Result] from [Niche]", "Business", "This is the simple system I use to create value and generate results in [Niche]. It doesn’t require chaos or perfection — just consistency and a clear offer."),
            TemplateItem("25", "My Biggest Mistake in [Field]", "Education", "The biggest mistake I made was trying to do too much too fast. I thought progress meant speed, but the truth is sustainable growth beats burnout every time."),
            TemplateItem("26", "This Is the Best Free Tool for [Task]", "Tech", "If you need a quick, useful solution for [Task], this is one of the best free tools I’ve found. It’s easy to use and helps save time immediately."),
            TemplateItem("27", "If I Had to Start Over, I’d Do This", "Business", "If I had to restart from zero, I’d keep it simpler, build faster, and focus on value before volume. The biggest lesson is that less complexity usually leads to better results."),
            TemplateItem("28", "The Perfect [Topic] Setup", "Tech", "Here’s the setup I recommend for [Topic]. It keeps things organized, easy to manage, and efficient for long-term use."),
            TemplateItem("29", "What I Do When I Feel Stuck", "Motivation", "When I get stuck, I stop trying to force inspiration and instead work on the next small step. Most progress happens when you keep moving, even if the task isn’t exciting."),
            TemplateItem("30", "My Workflow for [Goal]", "Business", "This is the workflow I use to stay consistent and productive. It helps me keep focus, reduce distractions, and move closer to my goal without wasting time."),
            TemplateItem("31", "Why [Problem] Is More Common Than You Think", "Education", "This problem is more common than many people realize because it’s often hidden behind daily routines and habits. Once you notice it, it becomes easier to fix."),
            TemplateItem("32", "The Biggest Myth About [Topic]", "Education", "The biggest myth is that success comes from talent alone. In reality, growth comes much more from effort, repetition, and learning from mistakes."),
            TemplateItem("33", "This Is Exactly How I [Achieve Goal]", "Motivation", "Here is the exact process I follow to achieve [Goal]. I keep it practical, realistic, and focused on progress rather than perfection."),
            TemplateItem("34", "What I Would Do Differently", "Business", "If I could go back, I’d make this change earlier. It would have saved time and given me better results much faster. That’s the value of learning from experience."),
            TemplateItem("35", "3 Strategies That Actually Work for [Goal]", "Marketing", "These are the three approaches I’ve seen actually work in real life. They’re not fancy, but they’re effective, repeatable, and easy to apply."),
            TemplateItem("36", "The 1-Minute Fix for [Problem]", "Lifestyle", "This is the fastest change I recommend if you’re dealing with [Problem]. It’s simple, quick, and surprisingly effective when done consistently."),
            TemplateItem("37", "My 30-Day Challenge: [Outcome]", "Motivation", "I committed to this 30-day challenge to improve [Outcome]. What I learned is that consistency matters more than intensity, and small daily actions create big change."),
            TemplateItem("38", "How I Learned [Skill] in 30 Days", "Education", "I wanted to learn [Skill] in 30 days, so I focused on simple, structured practice every day. This is how I made it manageable and improved faster than expected."),
            TemplateItem("39", "The Most Useful [Topic] Hack", "Tech", "This is the hack that saved me the most time and effort. It’s small, practical, and useful whether you’re a beginner or experienced."),
            TemplateItem("40", "The Hidden Cost of [Problem]", "Business", "Most people focus only on the immediate issue, but the real cost is often much bigger. This is what keeps the problem alive and why it matters to fix early."),
            TemplateItem("41", "I Tried [Popular Method] — Here’s the Truth", "Tech", "I tested this method because everyone seemed to recommend it. The outcome was mixed, and this is the honest truth about whether it actually works."),
            TemplateItem("42", "How to Get Results Without [Thing]", "Business", "You don’t always need more money, more tools, or more time to get results. In many cases, the better answer is to simplify and improve your strategy."),
            TemplateItem("43", "What Successful People Do Differently", "Motivation", "The difference is usually not talent — it’s habits, structure, and focus. Success comes from doing the right things consistently, even when they aren’t exciting."),
            TemplateItem("44", "The Beginner-Friendly Way to [Goal]", "Education", "If you’re just starting, don’t overcomplicate it. Begin with the simplest version, learn as you go, and build based on what actually works."),
            TemplateItem("45", "My Favorite [Product/Tool] for [Purpose]", "Tech", "This is my favorite tool for [Purpose]. It helps me work faster, stay organized, and keep my process simple when things get busy."),
            TemplateItem("46", "This Changed My [Area] Completely", "Lifestyle", "A small change completely transformed my perspective on [Area]. It sounds simple, but it made a huge difference in how I work and how I feel."),
            TemplateItem("47", "Things I Stopped Doing to Improve [Area]", "Lifestyle", "I stopped doing these things to improve [Area], and the difference was immediate. It made me more focused, calmer, and more productive."),
            TemplateItem("48", "The Fastest Way to [Outcome]", "Marketing", "The easiest way to see improvement is to remove friction. Focus on fewer steps, better messaging, and more clear actions, and results usually come faster."),
            TemplateItem("49", "What Happens When You [Action] Daily", "Motivation", "When you do this daily, the impact is bigger than you expect. It builds momentum, increases confidence, and makes it easier to reach your goals over time."),
            TemplateItem("50", "The Best [Topic] Advice for Beginners", "Education", "If you’re a beginner, focus on fundamentals and consistency. Learn the core principles, apply them regularly, and don’t compare yourself too early."),
            TemplateItem("51", "Here’s the Simple System I Use", "Business", "This is the system I use when I need to stay organized and productive. It keeps everything moving without creating stress or confusion."),
            TemplateItem("52", "The Most Overlooked Factor in [Outcome]", "Business", "Most people ignore this factor, but it matters a lot. Once you improve it, your results become easier to maintain and scale."),
            TemplateItem("53", "I Regret Not Doing This Sooner", "Motivation", "This is one of the things I wish I had started earlier. It would have saved me time and helped me reach better results sooner."),
            TemplateItem("54", "My Exact Setup for [Task]", "Tech", "Here’s my exact setup for [Task]. It’s simple, efficient, and designed to reduce friction so I can focus on the actual work."),
            TemplateItem("55", "How I Manage [Problem] Without Stress", "Lifestyle", "I manage [Problem] by simplifying the process and focusing on the essentials. That helps me stay calm and avoid unnecessary overwhelm."),
            TemplateItem("56", "What Nobody Tells You About [Industry/Niche]", "Business", "The real work in [Industry/Niche] is not just talent — it’s understanding systems, staying patient, and learning from feedback. That makes the difference over time."),
            TemplateItem("57", "Why [Common Mistake] Is So Toxic", "Education", "This mistake prevents progress because it creates confusion, wasted effort, and unrealistic expectations. Fixing it early makes everything easier."),
            TemplateItem("58", "A Simple Framework for [Goal]", "Business", "Here’s a simple framework I use to stay focused and move toward a goal without getting lost in the process."),
            TemplateItem("59", "What I’d Tell My Younger Self", "Motivation", "If I could talk to my younger self, I’d remind him that success takes time, patience, and consistency. Don’t rush the process."),
            TemplateItem("60", "The Real Reason You’re Not Getting [Result]", "Education", "You’re probably not getting the result because the process is too inconsistent or too complicated. Fix the system and the results improve naturally."),
            TemplateItem("61", "My Favorite Productivity Trick", "Lifestyle", "This is my favorite productivity trick: start with the hardest thing first. It helps remove delay and makes the rest of the day feel lighter."),
            TemplateItem("62", "How to Stay Motivated When [Situation]", "Motivation", "When you’re dealing with [Situation], motivation drops. The best way to stay consistent is to focus on your next step, not your entire journey."),
            TemplateItem("63", "This Simple Template Helps Me [Outcome]", "Business", "This simple framework helps me stay organized and consistently produce better results. I recommend it if you want clarity without complexity."),
            TemplateItem("64", "Here’s How I Plan My Week", "Lifestyle", "I plan my week by focusing on priority tasks first, leaving room for flexibility, and keeping my schedule realistic. That helps me maintain momentum."),
            TemplateItem("65", "My Process for [Project/Task]", "Business", "This is my process for [Project/Task]. It keeps the work organized and simple enough to repeat without burning out."),
            TemplateItem("66", "The 5-Step Formula for [Outcome]", "Marketing", "Here’s the 5-step formula I use to improve [Outcome]. It’s simple, effective, and easy to adapt to many situations."),
            TemplateItem("67", "The Easiest Beginner Plan for [Goal]", "Education", "If you’re starting from zero, this beginner plan keeps things simple and realistic. You don’t need a perfect strategy — you need a clear beginning."),
            TemplateItem("68", "How I Turned [Input] into [Output]", "Business", "This is how I transformed [Input] into [Output]. The key was clarity, disciplined execution, and improving based on feedback."),
            TemplateItem("69", "I Built This in 1 Hour", "Tech", "I built this in one hour to test the idea quickly. It wasn’t perfect, but it proved the value and gave me a way to improve it faster."),
            TemplateItem("70", "A Quick Breakdown of [Topic]", "Education", "Here’s a quick breakdown of [Topic] so it’s easier to understand. I’ll simplify the core idea and explain why it matters."),
            TemplateItem("71", "How I Use [Tool] to [Outcome]", "Tech", "This is how I use [Tool] to get better results with less effort. It saves time and helps me stay organized without adding complexity."),
            TemplateItem("72", "The Best Resources for Learning [Skill]", "Education", "If you want to improve at [Skill], these are the resources I recommend most. Start with the fundamentals and build from there."),
            TemplateItem("73", "The Simple Rule I Follow for [Topic]", "Lifestyle", "This is the one rule I follow: keep it simple and focus on what matters most. That helps me stay consistent and avoid overwhelm."),
            TemplateItem("74", "What I Would Charge for [Service/Offer]", "Business", "This is how I think about pricing for [Service/Offer]. It’s based on value, clarity, and what brings the most impact to the client."),
            TemplateItem("75", "A Common Mistake in [Industry]", "Business", "One common mistake in [Industry] is focusing only on volume instead of value. Better results usually come from clarity and consistency."),
            TemplateItem("76", "The Smartest Way to Start [Business/Project]", "Business", "The smartest way to start is to keep the first version simple, test the idea, and improve based on real feedback rather than assumptions."),
            TemplateItem("77", "Do This Instead of [Common Action]", "Lifestyle", "If you’re doing [Common Action], try this alternative instead. It’s simpler, less stressful, and usually delivers better long-term results."),
            TemplateItem("78", "My Honest Opinion on [Topic]", "Education", "Here’s my honest opinion on [Topic]. It’s not flashy, but it’s based on real experience and practical results, not hype."),
            TemplateItem("79", "The Secret to Better [Outcome]", "Marketing", "Better outcomes often come from removing distractions, improving clarity, and making the process easier to repeat. That’s the real lever."),
            TemplateItem("80", "What I Do Every Friday to Prepare for Success", "Lifestyle", "Every Friday I review my priorities, clean my workflow, and set up the next week so I start strong and stay organized."),
            TemplateItem("81", "How I Stay Consistent With [Habit]", "Lifestyle", "I stay consistent by keeping it simple enough to repeat. The goal isn’t intensity — it’s making the habit realistic and easy to maintain."),
            TemplateItem("82", "My Current [Topic] Strategy", "Business", "This is the strategy I’m currently using for [Topic]. It’s practical, flexible, and built around what actually works in real life."),
            TemplateItem("83", "This One Habit Helps Me [Outcome]", "Motivation", "This one habit has made the biggest difference for me because it improves my focus, saves time, and reduces friction in my routine."),
            TemplateItem("84", "I Tested 3 Ways to [Goal] — Here’s the Best", "Marketing", "I tested three different approaches to [Goal], and here’s the one that actually worked best in practice. This is the result of real trial and error."),
            TemplateItem("85", "A Beginner’s Crash Course on [Topic]", "Education", "If you’re brand new to [Topic], here’s a simple crash course to help you understand the fundamentals before going deeper."),
            TemplateItem("86", "How I Cut [Time/Cost] by [Amount]", "Business", "I cut [Time/Cost] by [Amount] by simplifying the process and removing unnecessary steps. The result was better efficiency without sacrificing quality."),
            TemplateItem("87", "This Is Why [Trend/Method] Is Overhyped", "Tech", "This idea is popular, but it’s often overhyped. It works in some situations, but it isn’t a universal fix — and that’s important to understand."),
            TemplateItem("88", "3 Questions I Ask Before Buying [Product/Service]", "Business", "Before buying anything, I ask these three questions: what problem does it solve, what’s the real value, and does it fit my process? That helps me avoid bad decisions."),
            TemplateItem("89", "How to Spot a Fake [Thing]", "Tech", "Here’s how I spot a fake [Thing]. It’s usually the details, consistency, and real proof behind the claims that reveal the truth."),
            TemplateItem("90", "My Quick Review of [Brand/Product]", "Tech", "This is my quick review of [Brand/Product]. I’ll cover what I liked, what I didn’t, and whether I’d recommend it."),
            TemplateItem("91", "The Step-by-Step Formula I Use to [Outcome]", "Business", "This is the formula I use to consistently improve [Outcome]. It keeps the process clear and prevents me from getting stuck in the wrong details."),
            TemplateItem("92", "The Most Important Thing to Focus on Right Now", "Motivation", "The most important thing right now is not perfection — it’s momentum. Focus on the next meaningful action and keep moving."),
            TemplateItem("93", "What Actually Works in [Industry/Niche]", "Business", "After trying many approaches, I’ve learned that what actually works is consistency, value, and clear communication. That is what drives real progress."),
            TemplateItem("94", "The Mistake That Cost Me [Money/Time]", "Business", "This was the mistake that cost me the most [Money/Time]. Once I recognized it, I changed the system and improved everything around it."),
            TemplateItem("95", "My Top 3 Tools for [Goal]", "Tech", "These are my top 3 tools for [Goal]. They help me stay more efficient, organized, and focused without adding unnecessary complexity."),
            TemplateItem("96", "What I Learned From My First [Project/Business]", "Business", "My first [Project/Business] taught me that execution matters more than perfection. The first version doesn’t need to be perfect — it needs to be useful."),
            TemplateItem("97", "A Simple Way to Improve [Skill]", "Education", "The easiest way to improve [Skill] is to practice consistently and focus on the fundamentals. Small, regular improvement beats random bursts of effort."),
            TemplateItem("98", "How to Get Better Results Without More Work", "Business", "You don’t have to work harder to get better results — you just need a cleaner system. Remove friction, improve focus, and outcomes usually improve."),
            TemplateItem("99", "The Difference Between [A] and [B]", "Education", "The difference between [A] and [B] is often not huge in theory, but huge in practice. The details matter, and that’s why context matters so much."),
            TemplateItem("100", "5 Ways to Make [Outcome] Easier", "Lifestyle", "Here are five practical ways to make [Outcome] easier. They’re simple, realistic, and designed to reduce stress rather than add more pressure."),
            TemplateItem("101", "My Daily Routine for [Goal]", "Lifestyle", "This is my daily routine for [Goal]. It keeps me focused, consistent, and productive without making life feel overwhelming."),
            TemplateItem("102", "I Built a [Thing] Using Only [Resource]", "Tech", "I built this [Thing] using only [Resource], and it was enough to create something useful. The lesson is that you don’t need a lot of resources to start."),
            TemplateItem("103", "Before You Buy [Product], Watch This", "Tech", "Before buying [Product], watch this first. It will help you understand whether it fits your needs, your budget, and your actual use case."),
            TemplateItem("104", "The Real Story Behind [Topic]", "Education", "The real story behind [Topic] is not the highlight reel — it’s the process, the setbacks, and the lessons learned along the way."),
            TemplateItem("105", "A Tiny Change That Improved [Area]", "Lifestyle", "This tiny change improved [Area] more than I expected. Small adjustments can create surprisingly strong results when repeated over time."),
            TemplateItem("106", "How I Handle [Challenge]", "Motivation", "When I face [Challenge], I break it into smaller steps and focus on the next action. That keeps the problem manageable and reduces emotional overload."),
            TemplateItem("107", "The Best Way to Learn [Skill] Fast", "Education", "The fastest way to learn [Skill] is to build something with it early, get feedback quickly, and repeat. Practice with purpose works faster than passive learning."),
            TemplateItem("108", "My Favorite Example of [Topic]", "Education", "This is one of my favorite examples of [Topic] because it shows the idea in a way that’s simple, relatable, and easy to remember."),
            TemplateItem("109", "This Is the Ultimate Checklist for [Task]", "Business", "If you’re doing [Task], this checklist keeps you organized and makes the process easier to manage from start to finish."),
            TemplateItem("110", "How I Turn [Input] into [Output] Every Week", "Business", "Here’s how I convert [Input] into [Output] every week without overcomplicating the process. It’s about clarity, repetition, and keeping momentum."),
            TemplateItem("111", "My 3 Favorite [Topic] Resources", "Education", "These are my top three resources for [Topic]. They’re practical, actionable, and helpful whether you’re just getting started or trying to improve."),
            TemplateItem("112", "How to Make [Outcome] More Efficient", "Business", "To make [Outcome] more efficient, reduce wasted motion, simplify steps, and focus on what actually creates value."),
            TemplateItem("113", "The Only Way to Grow in [Niche]", "Business", "The only way to grow in [Niche] is to combine consistency, value, and patience. Shortcuts may look attractive, but real growth takes time."),
            TemplateItem("114", "What Changed My Mind About [Topic]", "Education", "This was the moment I realized my approach to [Topic] was wrong. Once I changed my perspective, progress became much easier."),
            TemplateItem("115", "3 Cheap Ways to Improve [Area]", "Lifestyle", "Here are three inexpensive ways to improve [Area] without spending much. They’re practical, easy to do, and surprisingly effective."),
            TemplateItem("116", "What I Do When I Don’t Feel Like [Action]", "Motivation", "When I don’t feel like [Action], I lower the bar and do the smallest meaningful step. That keeps me moving even on low-energy days."),
            TemplateItem("117", "My Honest Breakdown of [Industry/Trend]", "Tech", "Here’s my honest breakdown of [Industry/Trend]. Some parts are useful, some are overrated, and some are only valuable in the right context."),
            TemplateItem("118", "A Better Alternative to [Popular Method]", "Tech", "If you’re tired of [Popular Method], this is a better alternative that’s simpler, more practical, and easier to maintain."),
            TemplateItem("119", "Who Should Not Use [Product/Strategy]", "Business", "This isn’t for everyone. If you need [specific situation], then this product or strategy may create more friction than value."),
            TemplateItem("120", "I Asked a Pro How to [Goal], Here’s What They Said", "Education", "I asked a professional how to do [Goal], and they gave me this advice. It’s simple, realistic, and worth following if you want better results.")
        )
    }
}

data class TemplateItem(
    val id: String,
    val title: String,
    val category: String,
    val content: String
)

class TemplateAdapter(
    private val onCopy: (TemplateItem) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {

    private val items = mutableListOf<TemplateItem>()

    fun submitList(newItems: List<TemplateItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_template, parent, false)
        return TemplateViewHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onCopy)
    }

    override fun getItemCount(): Int = items.size

    class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val categoryText: TextView = itemView.findViewById(R.id.categoryText)
        private val contentText: TextView = itemView.findViewById(R.id.contentText)
        private val copyButton: Button = itemView.findViewById(R.id.copyButton)

        fun bind(item: TemplateItem, onCopy: (TemplateItem) -> Unit) {
            titleText.text = item.title
            categoryText.text = item.category
            contentText.text = item.content
            copyButton.setOnClickListener {
                onCopy(item)
            }
        }
    }
}
