package application.util;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

public class Gits {

	public static void push(String fileName){
		try {

			URI uri = Gits.class.getResource("/").toURI();
			String path = uri.getPath();
			System.out.println(path);

			String dir = new File(path).getParentFile().getAbsolutePath() + File.separator + ".git";
			Git git = Git.open(new File(dir));

			Repository repository = git.getRepository();
			System.out.println(repository.getBranch());

			// git log
			LogCommand logCommand = git.log();
            Iterator<RevCommit> iterator= logCommand.call().iterator();
            while (iterator.hasNext()) {
                RevCommit revCommit = iterator.next();
                System.out.println(new String(revCommit.getRawBuffer()));
            }

            /******************************************************************************
             * ------------------------- git add 用法 -------------------------
             * 表示把<path>中所有tracked文件中被修改过或已删除文件和所有untracted的文件信息添加到索引库。
             * addFilepattern(". -A").call()
             * addFilepattern("README").call()
             * --------------------------------------------------------------
             *****************************************************************************/
            git.add().addFilepattern(".").call();

            // git commit
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            StringBuffer message = new StringBuffer();
            message.append("=======");
            message.append("update ");
            message.append(fileName);
            message.append(" on ");
            message.append(sf.format(new Date()));
            message.append("=======");
            git.commit().setAll(true).setAuthor("wangli0", "xx@qq.com").setMessage(message.toString()).call();

	        // https 方式提交
            // git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,password)).call();

            // ssh方式提交
            AllowHostsCredentialsProvider allowHosts = new AllowHostsCredentialsProvider();
            git.push().setCredentialsProvider(allowHosts).call();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
