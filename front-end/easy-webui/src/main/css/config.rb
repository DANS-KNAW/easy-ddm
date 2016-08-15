Encoding.default_external = "utf-8"
# Require any additional compass plugins here.
require 'bootstrap-sass'
require 'font-awesome-sass'

# Set this to the root of your project when deployed:
http_path = "/"
sass_dir = "sass"
css_dir = "../java/nl/knaw/dans/easy/web/template/css"
images_dir = "../java/nl/knaw/dans/easy/web/template/css"
fonts_dir = "../java/nl/knaw/dans/easy/web/template/css/fonts"

# You can select your preferred output style here (can be overridden via the command line):
# output_style = :expanded or :nested or :compact or :compressed
output_style = :compressed
# To enable relative paths to assets via compass helper functions. Uncomment:
relative_assets = true

# To disable debugging comments that display the original location of your selectors. Uncomment:
line_comments = false


# If you prefer the indented syntax, you might want to regenerate this
# project again passing --syntax sass, or you can uncomment this:
# preferred_syntax = :sass
# and then run:
# sass-convert -R --from scss --to sass sass scss && rm -rf sass && mv scss sass
#
# TIP: Use ``compass watch`` in the CSS folder to compile the files