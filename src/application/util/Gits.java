package application.util;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import application.Constants;

/**
 * @see http://wiki.eclipse.org/JGit/User_Guide
 * @see http://download.eclipse.org/jgit/site/4.9.0.201710071750-r/apidocs/index.html
 * @author Limitless
 */
public class Gits {

	public interface Callback {
		void success();
		void error(Exception e);
	}

	public static void push(Callback callback){
		try {
			// URI uri = Gits.class.getResource("/").toURI();
			String projectDir = System.getProperty("user.dir");
			String deployDir = Preferences.get(Constants.CONFIG_DEPLOY_PATH);
			// =============== 加密  ===============
			String realDeployDir = null;
			if("".equals(deployDir)){
				realDeployDir = projectDir;
			}else{
				realDeployDir = deployDir;
			}
			L.D("begin deploy : " + realDeployDir + File.separator + "encrypt.db");
			File encryptdb = new File(realDeployDir,"encrypt.db");
			if(!encryptdb.exists()){
				encryptdb.createNewFile();
			}

			byte[] datas = IOUtils.toByteArray(new FileInputStream("notebook.db"));

			String secret = Preferences.get(Constants.CONFIG_SECRET);
			if("".equals(secret)){
				throw new IllegalArgumentException("未设置加密秘钥");
			}

			byte[] encryptDatas = PBECoder.encript(datas, secret, "13572468".getBytes());
			System.out.println(encryptDatas.length);
			FileUtils.writeByteArrayToFile(encryptdb, encryptDatas);

			String gitResitory = realDeployDir + File.separator + ".git";
			Git git = Git.open(new File(gitResitory));

			Repository repository = git.getRepository();
			System.out.println(repository.getBranch());

			// git log
			LogCommand logCommand = git.log();
            Iterator<RevCommit> iterator= logCommand.call().iterator();
            while (iterator.hasNext()) {
                RevCommit revCommit = iterator.next();
                // System.out.println(new String(revCommit.getRawBuffer()));
            }

            /******************************************************************************
             * ------------------------- git add 用法 -------------------------
             * 表示把<path>中所有tracked文件中被修改过或已删除文件和所有untracted的文件信息添加到索引库。
             * addFilepattern(". -A").call()
             * addFilepattern("README").call()
             * --------------------------------------------------------------
             *****************************************************************************/

            git.add().addFilepattern("encrypt.db").call();
            // git commit
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            StringBuffer message = new StringBuffer();
            message.append("=======");
            message.append(" :smile: ");
            message.append("update Notebook.db");
            message.append(" on ");
            message.append(sf.format(new Date()));
            message.append(" =======");
            // git.commit().setAll(true) 相当于 git commit -a,等于提交全部文件,不推荐。
            // git.commit().setAll(true).setAuthor("deeper", "kaiyuan@qq.com").setMessage(message.toString()).call();
            git.commit().setAuthor("deeper", "kaiyuan@qq.com").setMessage(message.toString()).call();

            String git_user = Preferences.get(Constants.CONFIG_GIT_USER);
            String git_pass = Preferences.get(Constants.CONFIG_GIT_PASSWORD);

            if(!"".equals(git_user) || !"".equals(git_pass)){
    	        // https 方式提交
                git.push().setCredentialsProvider(new UsernamePasswordCredentialsProvider(git_user,git_pass)).call();
            } else {
                // ssh方式提交
                AllowHostsCredentialsProvider allowHosts = new AllowHostsCredentialsProvider();
                git.push().setCredentialsProvider(allowHosts).call();

            }
            callback.success();

		} catch (Exception e) {
			callback.error(e);
		}
	}
}
