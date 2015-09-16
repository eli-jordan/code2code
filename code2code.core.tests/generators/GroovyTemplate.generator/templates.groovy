String templateText = '''Project report:

We have currently ${tasks.size} number of items with a total duration of $duration.
<% tasks.each { %>- $it.summary
<% } %>

'''