//
//  AddUserViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-28.
//
import UIKit

class AddUserViewController: UIViewController {
    
    @IBOutlet weak var tfUsername: UITextField!
    @IBOutlet weak var tfName: UITextField!
    @IBOutlet weak var tfPassword: UITextField!
    @IBOutlet weak var tfConfirmPassword: UITextField!
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    
    private var user: User?
    private var isUserAdmin = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    }
    
    func with(_ adminUser: User) {
        user = adminUser
    }
    
    @IBAction func onBack() {
        let controller = UIAlertController(title: "Confirm Exit", message: "Are you sure you want to exit? Your changes will be lost.", preferredStyle: .alert)
        controller.addAction(UIAlertAction(title: "No", style: .cancel))
        controller.addAction(UIAlertAction(title: "Yes", style: .destructive) {_ in
            self.dismiss(animated: true)
        })
        present(controller, animated: true)
    }
    
    @IBAction func onSubmit() {
        
    }
    
    @IBAction func onSelectAdmin() {
        isUserAdmin = true
    }
    
    @IBAction func onSelectNonAdmin() {
        isUserAdmin = false
    }
}
