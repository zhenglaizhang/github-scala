val foo = "foo"
s"it's $foo $$"

val s = "%02d: name = %s".format(5, "dean")

val multiLine = s"123\n$foo\n123"
val multiLineRaw = raw"123\n$foo\n123"

// we can define our own string interpolators