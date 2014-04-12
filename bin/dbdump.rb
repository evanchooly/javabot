#! /usr/local/bin/ruby

require 'date'
require 'fileutils'

@backupDir = DateTime.now.strftime("%Y-%m-%d-%H%M")
@backupRoot = File.absolute_path("#{Dir.home}/backup/javabot/")
@dir = File.absolute_path(@backupRoot + '/' + @backupDir)
@db='javabot'

def backup(collection)
  puts "Backing up #{collection} to #{@dir}"
  %x( mongodump -d #{@db} -c #{collection} -o #{@dir})
end

def export(*collections)
  dest='/var/www/localhost/htdocs/db-export/javabot'
  collections.each { |collection|
    puts "Exporting #{collection} to #{dest}"
  }
 end

def cleanup
  count=7
  entries = %x( ls #{@backupRoot} ).split
  entries.reverse.each_with_index { |dir, index|
    if index > 13 and dir != '.' and dir != '..'
      puts "Removing expired backup #{dir}"
      FileUtils.remove_dir(@backupRoot + '/' + dir)
    end
  }
end

def prep
  unless Dir.exists?(@dir)
    FileUtils.mkdir_p(@dir)
  end
end

prep

backup 'admins'
backup 'apis'
backup 'changes'
backup 'channels'
backup 'classes'
backup 'configuration'
backup 'events'
backup 'factoids'
backup 'fields'
backup 'karma'
backup 'logs'
backup 'methods'
backup 'registrations'
backup 'shuns'

export 'apis', 'classes', 'methods', 'fields', 'factoids'

puts %x( rm -f #{@backupRoot}/current )

cleanup

puts %x( ln -svf #{@dir} #{@backupRoot}/current )
