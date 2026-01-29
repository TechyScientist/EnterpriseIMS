//
//  DeleteUserViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-29.
//
import UIKit

class DeleteUserViewController: UIViewController {
    
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    @IBOutlet weak var errorView: UIView!
    @IBOutlet weak var successView: UIView!
    @IBOutlet weak var lbError: UILabel!
    @IBOutlet weak var lbSuccess: UILabel!
    @IBOutlet weak var btUserSelect: UIButton!
    @IBOutlet weak var btSubmit: UIButton!
    
    private var users: [User] = []
    private var admin: User?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        refreshUserList()
    }
    
    func refreshUserList() {
        btSubmit.isEnabled = false
        btUserSelect.isHidden = true
        indicator.isHidden = false
        let request = URLRequest(url: URL(string: "https://wildfly.johnnyconsole.com:8443/ims/api/user/get-all?except=\(admin!.username)&auth-user=\(admin!.username)")!)
        let session = URLSession(configuration: .default)
        session.dataTask(with: request) { data, response, _ in
            let responseCode = (response as! HTTPURLResponse).statusCode
            
            DispatchQueue.main.async { [self] in
                guard responseCode != OK, let data = data else {
                    //TODO: Implement error checking from API response codes
                    return
                }
                self.users = try! JSONDecoder().decode([User].self, from: data)
                
                if(!self.users.isEmpty) {
                    var actions: [UIAction] = []
                    
                    for i in self.users.indices {
                        actions.append(UIAction(title: "\(users[i].name) (\(users[i].username))", handler: {action in
                            self.onChangeUser(i)
                        }))
                    }
                    
                    btUserSelect.menu = UIMenu(options: .singleSelection, children: actions)
                    btUserSelect.setTitle(actions.first!.title, for: .normal)
                    (btUserSelect.menu!.children.first as? UIAction)?.state = .on
                    btUserSelect.changesSelectionAsPrimaryAction = false
                    btUserSelect.showsMenuAsPrimaryAction = true
                    btUserSelect.isEnabled = true
                    btSubmit.isEnabled = true
                } else {
                    btUserSelect.setTitle("No Users Found", for: .normal)
                    btUserSelect.isEnabled = false
                    btSubmit.isEnabled = false
                }
                indicator.isHidden = true
                btUserSelect.isHidden = false
            }
        }.resume()
    }
    
    func with(_ adminUser: User) {
        admin = adminUser
    }
    
    func onChangeUser(_ senderIndex: Int) {
        (btUserSelect.menu!.children[senderIndex] as! UIAction).state = .on
        btUserSelect.setTitle((btUserSelect.menu!.children[senderIndex] as! UIAction).title, for: .normal)
    }
    
    @IBAction func onSubmit() {
        errorView.isHidden = true
        successView.isHidden = true
        indicator.isHidden = false
        var username = btUserSelect.title(for: .normal)!
        username = username[username.index(after: username.firstIndex(of: "(")!)..<username.firstIndex(of: ")")!].lowercased()
        var request = URLRequest(url: URL(string: "https://wildfly.johnnyconsole.com:8443/ims/api/user/delete")!)
        request.httpMethod = "POST"
        request.httpBody = "username=\(username)&auth-user=\(admin!.username)".data(using: .utf8)
        let session = URLSession(configuration: .default)
        session.dataTask(with: request) { [self] _, response, _ in
            let responseCode = (response as! HTTPURLResponse).statusCode
            
            DispatchQueue.main.async {
                if(responseCode == ACCEPTED) {
                    self.refreshUserList()
                    self.successView.isHidden = false
                    self.lbSuccess.attributedText = NSAttributedString(string: "The user \(username) was deleted successfully.", attributes: [.font: UIFont.boldSystemFont(ofSize: CGFloat(17))])
                } else {
                    //TODO: Handle error codes thrown by the API
                }
                self.indicator.isHidden = true
            }
        }.resume()
    }
    
    @IBAction func onBack() {
        dismiss(animated: true)
    }
}
