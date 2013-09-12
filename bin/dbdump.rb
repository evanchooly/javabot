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
  puts entries
  entries.reverse.each_with_index { |dir, index|
    if index > 6 and dir != '.' and dir != '..'
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

backup 'admin'
backup 'apis'
backup 'changes'
backup 'channel'
backup 'classes'
backup 'configuration'
backup 'configuration_operations'
backup 'factoids'
backup 'fields'
backup 'karma'
backup 'logs'
backup 'methods'
backup 'shun'

export 'schema', 'apis', 'classes', 'methods', 'factoids'

puts %x( rm -f #{@backupRoot}/current )

cleanup

puts %x( ln -svf #{@dir} #{@backupRoot}/current )
